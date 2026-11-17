#!/usr/bin/env python3
"""将现有生产配置中的凭证迁移到 systemd 环境文件。"""

import os
from pathlib import Path
import re
import subprocess

import yaml

CONFIG = Path("/opt/services/club/application-prod.yml")
ENV_FILE = Path("/etc/club/club.env")
DROP_IN = Path("/etc/systemd/system/club.service.d/production-env.conf")
KEYS = {
    "DATABASE_USERNAME": ("mysql", "username"),
    "DATABASE_PASSWORD": ("mysql", "password"),
    "REDIS_PASSWORD": ("redis", "password"),
    "QINIU_ACCESS_KEY": ("qiniu", "access-key"),
    "QINIU_SECRET_KEY": ("qiniu", "secret-key"),
    "BOT_API_TOKEN": ("bot-api-token",),
}


def resolve(value):
    """解析现有配置中的环境变量默认值。"""
    if not isinstance(value, str):
        raise ValueError("凭证配置格式不正确")
    match = re.fullmatch(r"\$\{([A-Za-z_][A-Za-z0-9_]*)(?::(.*))?\}", value)
    if match:
        name, default = match.groups()
        value = os.environ.get(name, default)
    if not value or "\n" in value or "\r" in value:
        raise ValueError("凭证缺失或包含换行")
    return value


def main():
    """写入受限的 systemd 环境文件和服务配置。"""
    if os.geteuid() != 0:
        raise RuntimeError("环境配置需要管理员权限")
    if ENV_FILE.exists() or DROP_IN.exists():
        raise FileExistsError("环境文件或服务配置已存在，需先核对")
    with CONFIG.open(encoding="utf-8") as source:
        app = yaml.safe_load(source)["app"]
    lines = []
    for name, path in KEYS.items():
        value = app
        for key in path:
            value = value[key]
        value = resolve(value).replace("\\", "\\\\").replace('"', '\\"')
        lines.append(f'{name}="{value}"\n')
    ENV_FILE.parent.mkdir(mode=0o700, parents=True, exist_ok=True)
    DROP_IN.parent.mkdir(mode=0o755, parents=True, exist_ok=True)
    fd = os.open(ENV_FILE, os.O_WRONLY | os.O_CREAT | os.O_EXCL, 0o600)
    with os.fdopen(fd, "w", encoding="utf-8") as target:
        target.writelines(lines)
    try:
        with DROP_IN.open("x", encoding="utf-8") as target:
            target.write(f"[Service]\nEnvironmentFile={ENV_FILE}\n")
        subprocess.run(["/usr/bin/systemctl", "daemon-reload"], check=True)
    except Exception:
        DROP_IN.unlink(missing_ok=True)
        ENV_FILE.unlink(missing_ok=True)
        raise
    print("生产环境变量已配置；服务将在下次重启时加载。")


if __name__ == "__main__":
    main()
