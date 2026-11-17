#!/usr/bin/env python3
"""接收前端发布包，并在文件切换失败时恢复站点。"""

import fcntl
import hashlib
import os
from pathlib import Path, PurePosixPath
import shutil
import subprocess
import sys
import tarfile
import tempfile
import time

ROOT = Path("/opt/1panel/www/sites/club.luowb.cn/index")
MAX_SIZE = 200 * 1024 * 1024


def fingerprint(root):
    """根据相对路径和文件内容生成站点指纹。"""
    digest = hashlib.sha256()
    for entry in sorted(root.rglob("*"), key=lambda path: path.relative_to(root).as_posix()):
        if entry.is_symlink():
            raise ValueError("站点包含不支持的符号链接")
        if entry.is_file():
            name = entry.relative_to(root).as_posix().encode("utf-8")
            digest.update(len(name).to_bytes(4, "big"))
            digest.update(name)
            file_hash = hashlib.sha256()
            with entry.open("rb") as source:
                for chunk in iter(lambda: source.read(1024 * 1024), b""):
                    file_hash.update(chunk)
            digest.update(file_hash.digest())
    return digest.hexdigest()


def replace_contents(source, target):
    """替换站点目录内容，保留作为挂载点的根目录。"""
    subprocess.run(
        ["sudo", "-n", "/usr/bin/find", str(target), "-mindepth", "1", "-depth", "-delete"],
        check=True,
    )
    for entry in source.iterdir():
        if entry.is_symlink():
            (target / entry.name).symlink_to(os.readlink(entry))
        elif entry.is_dir():
            shutil.copytree(entry, target / entry.name, symlinks=True)
        else:
            shutil.copy2(entry, target / entry.name)


def main():
    """校验前端资源，备份当前版本并发布。"""
    if not ROOT.is_dir():
        raise RuntimeError("站点目录不可用")
    with open(Path.home() / ".club-frontend-deploy.lock", "w") as lock:
        fcntl.flock(lock, fcntl.LOCK_EX)
        with tempfile.TemporaryDirectory(prefix="club-frontend-") as directory:
            stage = Path(directory) / "new"
            backup = Path(directory) / "old"
            stage.mkdir()
            total = 0
            with tarfile.open(fileobj=sys.stdin.buffer, mode="r|*") as archive:
                for member in archive:
                    path = PurePosixPath(member.name)
                    if path.is_absolute() or ".." in path.parts or not (member.isfile() or member.isdir()):
                        raise ValueError("发布包含不允许的文件")
                    total += member.size
                    if total > MAX_SIZE:
                        raise ValueError("发布文件过大")
                    dest = stage.joinpath(*path.parts)
                    if member.isdir():
                        dest.mkdir(parents=True, exist_ok=True)
                        dest.chmod(0o755)
                    else:
                        dest.parent.mkdir(parents=True, exist_ok=True)
                        with archive.extractfile(member) as source, dest.open("xb") as output:
                            shutil.copyfileobj(source, output)
                        dest.chmod(0o644)
            if not (stage / "index.html").is_file():
                raise ValueError("发布缺少站点入口")
            shutil.copytree(ROOT, backup, symlinks=True)
            backup_dir = Path.home() / ".deploy-backups" / "club-frontend"
            backup_dir.mkdir(mode=0o700, exist_ok=True)
            archive_path = backup_dir / time.strftime("actions-%Y%m%d-%H%M%S.tar.gz")
            with tarfile.open(archive_path, "x:gz") as archive:
                archive.add(backup, arcname=".")
            try:
                replace_contents(stage, ROOT)
                if not (ROOT / "index.html").is_file():
                    raise RuntimeError("站点入口不可用")
            except Exception:
                replace_contents(backup, ROOT)
                raise
            print(f"前端部署成功，备份：{archive_path}")


if __name__ == "__main__":
    command = os.environ.get("SSH_ORIGINAL_COMMAND", "")
    if command == "fingerprint":
        print(fingerprint(ROOT))
    elif not command and len(sys.argv) == 3 and sys.argv[1] == "--fingerprint":
        print(fingerprint(Path(sys.argv[2])))
    elif not command and len(sys.argv) == 1:
        main()
    else:
        raise ValueError("不支持的发布命令")
