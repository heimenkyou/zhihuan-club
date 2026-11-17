#!/usr/bin/env python3
"""接收后端发布包，并在健康检查失败时恢复上一版本。"""

import fcntl
import hashlib
import json
import os
from pathlib import Path
import shutil
import stat
import subprocess
import sys
import tarfile
import tempfile
import time
import urllib.error
import urllib.request

ROOT = Path("/opt/services/club")
STAGED_JAR = Path("/home/club-deploy/staging/app.jar")
FILES = {"manifest.json": 4096, "application-prod.yml": 1024 * 1024}


def healthy():
    """等待后端公开接口响应，确认应用完成启动。"""
    deadline = time.monotonic() + 90
    while time.monotonic() < deadline:
        try:
            with urllib.request.urlopen("http://127.0.0.1:12321/api/awards", timeout=3) as response:
                if response.status == 200 and json.load(response).get("code") == "0":
                    return True
        except (OSError, ValueError, urllib.error.URLError):
            pass
        time.sleep(2)
    return False


def main():
    """校验发布包，备份线上版本并执行切换。"""
    if os.geteuid() != 0:
        raise RuntimeError("部署程序需要管理员权限")
    with open("/run/lock/club-deploy.lock", "w") as lock:
        fcntl.flock(lock, fcntl.LOCK_EX)
        with tempfile.TemporaryDirectory(prefix=".club-deploy-", dir=ROOT) as directory:
            stage = Path(directory)
            seen = set()
            with tarfile.open(fileobj=sys.stdin.buffer, mode="r|*") as archive:
                for member in archive:
                    if member.name not in FILES or member.name in seen or not member.isfile():
                        raise ValueError("发布包含不允许的文件")
                    if member.size <= 0 or member.size > FILES[member.name]:
                        raise ValueError("发布文件大小不合法")
                    seen.add(member.name)
                    with archive.extractfile(member) as source, (stage / member.name).open("wb") as dest:
                        shutil.copyfileobj(source, dest)
            if seen != FILES.keys():
                raise ValueError("发布文件不完整")
            with (stage / "manifest.json").open(encoding="utf-8") as source:
                expected = json.load(source)["sha256"]
            if not isinstance(expected, str) or len(expected) != 64:
                raise ValueError("发布包校验值不合法")
            descriptor = os.open(STAGED_JAR, os.O_RDONLY | os.O_NOFOLLOW)
            with os.fdopen(descriptor, "rb") as source, (stage / "app.jar").open("wb") as dest:
                info = os.fstat(source.fileno())
                if not stat.S_ISREG(info.st_mode) or not 0 < info.st_size <= 200 * 1024 * 1024:
                    raise ValueError("暂存 JAR 不合法")
                shutil.copyfileobj(source, dest)
            with (stage / "app.jar").open("rb") as source:
                actual = hashlib.file_digest(source, "sha256").hexdigest()
            if actual != expected:
                raise ValueError("JAR 校验失败")
            backup = ROOT / ".deploy-backups" / time.strftime("actions-%Y%m%d-%H%M%S")
            backup.mkdir(parents=True, mode=0o700)
            for name in ("app.jar", "application-prod.yml"):
                shutil.copy2(ROOT / name, backup / name)
            try:
                for name in ("app.jar", "application-prod.yml"):
                    os.chmod(stage / name, 0o640 if name.endswith(".yml") else 0o644)
                    shutil.chown(stage / name, user="heimenkyou", group="heimenkyou")
                    os.replace(stage / name, ROOT / name)
                subprocess.run(["/usr/bin/systemctl", "restart", "club.service"], check=True)
                if not healthy():
                    raise RuntimeError("后端健康检查失败")
            except Exception:
                for name in ("app.jar", "application-prod.yml"):
                    shutil.copy2(backup / name, ROOT / name)
                subprocess.run(["/usr/bin/systemctl", "restart", "club.service"], check=False)
                raise
            print(f"后端部署成功，备份：{backup}")


if __name__ == "__main__":
    main()
