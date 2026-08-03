#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Vue 3 前端部署脚本。

当前配置：
- SSH 主机：luowb
- 远端目录：/opt/1panel/www/sites/club.luowb.cn/index
- 构建命令：pnpm run build

流程：构建 -> 压缩 dist -> 上传 /tmp -> 备份旧站点 ->
使用 sudo 清空 1Panel 的 root 目录 -> 复制新文件 -> 清理旧备份。
"""

from __future__ import annotations

import shlex
import shutil
import subprocess
import sys
import tempfile
import uuid
from pathlib import Path


# =============================================================================
# 配置区域：新项目通常只改这三项
# =============================================================================

# ~/.ssh/config 中的主机别名。
SSH_HOST = "luowb"

# 1Panel 创建的网站根目录。
REMOTE_DIR = "/opt/1panel/www/sites/club.luowb.cn/index"

# 临时压缩包前缀，仅用于方便识别。
ARCHIVE_PREFIX = "club-frontend"


# =============================================================================
# 常规配置：通常不用改
# =============================================================================

DIST_DIR = Path("dist")
BUILD_SCRIPT = "build"
KEEP_BACKUPS = 5

# None 时根据锁文件自动识别 pnpm、npm、yarn 或 bun。
PACKAGE_MANAGER: str | None = None
SEVEN_ZIP = "7z"


# 通过 SSH 直接交给服务器 bash 执行，不需要在服务器保存此脚本。
REMOTE_SCRIPT = r"""
set -Eeuo pipefail

target="$1"
archive="$2"
keep="$3"

fail() {
    echo "部署失败：$*" >&2
    exit 1
}

[[ "$target" == /opt/1panel/www/sites/*/index ]] ||
    fail "远端目录不在允许范围内：$target"
[[ "$archive" == /tmp/*.7z ]] ||
    fail "压缩包必须位于 /tmp"
[[ -f "$archive" ]] ||
    fail "服务器上没有找到压缩包"
[[ -d "$target" ]] ||
    fail "站点目录不存在，请先在 1Panel 中创建网站：$target"
[[ "$keep" =~ ^[1-9][0-9]*$ ]] ||
    fail "备份数量必须是正整数"

command -v 7z >/dev/null 2>&1 || fail "服务器没有安装 7z"
command -v tar >/dev/null 2>&1 || fail "服务器没有安装 tar"

site_name="$(basename "$(dirname "$target")")"
timestamp="$(date +%Y%m%d-%H%M%S-%N)"

# 备份放在普通用户家目录，因此备份和清理都不需要 sudo。
backup_dir="$HOME/.deploy-backups/frontend/$site_name"
backup_file="$backup_dir/$timestamp.tar.gz"
mkdir -p "$backup_dir"

# 新版本先解压到 /tmp，确认 index.html 存在后再改正式目录。
staging="$(mktemp -d "/tmp/vue-deploy-${site_name}.XXXXXX")"

has_backup=false
site_changed=false
finished=false

cleanup() {
    status=$?

    # 正式目录已经修改但部署未完成时，尝试恢复旧版本。
    if [[ "$finished" != true && "$site_changed" == true && "$has_backup" == true ]]; then
        echo "部署中断，正在恢复旧站点……" >&2
        rollback_dir="$(mktemp -d "/tmp/vue-deploy-rollback-${site_name}.XXXXXX")"

        if tar -C "$rollback_dir" -xzf "$backup_file"; then
            sudo -n /usr/bin/find "$target" \
                -mindepth 1 \
                -maxdepth 1 \
                -exec /usr/bin/rm -rf -- {} +

            sudo -n /usr/bin/cp -r -- "$rollback_dir"/. "$target"/
        fi

        rm -rf "$rollback_dir"
    fi

    rm -rf "$staging"
    rm -f "$archive"
    exit "$status"
}
trap cleanup EXIT

# 普通用户在 /tmp 解压。
7z x "$archive" "-o$staging" -y >/dev/null
[[ -f "$staging/index.html" ]] || fail "压缩包根目录中没有 index.html"

# 备份旧站点，包含隐藏文件。
if find "$target" -mindepth 1 -print -quit | grep -q .; then
    tar -C "$target" -czf "$backup_file" .
    has_backup=true
    echo "旧版本已备份：$backup_file"
fi

# 清空 1Panel 的 root 目录，包括隐藏文件。
sudo -n /usr/bin/find "$target" \
    -mindepth 1 \
    -maxdepth 1 \
    -exec /usr/bin/rm -rf -- {} +

site_changed=true

# 以 root 身份复制，新文件归 root 所有。
sudo -n /usr/bin/cp -r -- "$staging"/. "$target"/

# 统一站点权限，确保下次普通用户能够读取并备份。
sudo -n /usr/bin/find "$target" \
    -type d \
    -exec /usr/bin/chmod 755 {} +

sudo -n /usr/bin/find "$target" \
    -type f \
    -exec /usr/bin/chmod 644 {} +

[[ -f "$target/index.html" ]] || fail "部署后没有找到 index.html"

finished=true

# 只保留最近 keep 份备份。
mapfile -t old_backups < <(
    find "$backup_dir" \
        -maxdepth 1 \
        -type f \
        -name '*.tar.gz' \
        -printf '%T@ %p\n' |
        sort -rn |
        awk -v keep="$keep" 'NR > keep {sub(/^[^ ]+ /, ""); print}'
)

if ((${#old_backups[@]} > 0)); then
    rm -f -- "${old_backups[@]}"
fi

echo "前端部署完成：$target"
"""


def run(
    command: list[str],
    *,
    cwd: Path | None = None,
    input_text: str | None = None,
) -> None:
    """执行命令；远端脚本使用字节发送，避免 Windows 把 LF 转成 CRLF。"""
    print(f"\n> {shlex.join(command)}")

    input_bytes = None
    if input_text is not None:
        normalized = (
            input_text
            .replace("\r\n", "\n")
            .replace("\r", "\n")
        )
        input_bytes = normalized.encode("utf-8")

    subprocess.run(
        command,
        cwd=cwd,
        input=input_bytes,
        check=True,
    )

def command_path(command: str) -> str:
    """取得命令实际路径，找不到时直接报错。"""
    path = shutil.which(command)
    if path is None:
        raise RuntimeError(f"没有找到命令：{command}")
    return path


def detect_package_manager() -> str:
    """根据锁文件自动识别包管理器。"""
    if PACKAGE_MANAGER:
        return command_path(PACKAGE_MANAGER)

    candidates = (
        ("pnpm-lock.yaml", "pnpm"),
        ("package-lock.json", "npm"),
        ("yarn.lock", "yarn"),
        ("bun.lock", "bun"),
        ("bun.lockb", "bun"),
    )

    for lock_file, manager in candidates:
        if Path(lock_file).is_file():
            return command_path(manager)

    raise RuntimeError("没有找到包管理器锁文件，请设置 PACKAGE_MANAGER")


def build_project(package_manager: str) -> None:
    """构建项目并检查 dist/index.html。"""
    print("开始构建前端项目……")
    run([package_manager, "run", BUILD_SCRIPT])

    index_file = DIST_DIR / "index.html"
    if not index_file.is_file():
        raise RuntimeError(f"构建完成后没有找到：{index_file}")


def create_archive(archive_path: Path, seven_zip: str) -> None:
    """压缩 dist 内部内容，不额外套一层 dist 目录。"""
    print("正在压缩 dist 目录……")

    if archive_path.exists():
        archive_path.unlink()

    run(
        [seven_zip, "a", "-t7z", "-mx=5", "-y", str(archive_path), "."],
        cwd=DIST_DIR.resolve(),
    )

    if not archive_path.is_file():
        raise RuntimeError("压缩失败，未生成压缩包")


def deploy_remote(remote_archive: str) -> None:
    """把远端部署逻辑交给服务器 bash。"""
    arguments = [
        shlex.quote(REMOTE_DIR),
        shlex.quote(remote_archive),
        str(KEEP_BACKUPS),
    ]
    remote_command = "bash -s -- " + " ".join(arguments)

    print("正在服务器端备份并部署……")
    run(["ssh", SSH_HOST, remote_command], input_text=REMOTE_SCRIPT)


def cleanup_remote_archive(remote_archive: str) -> None:
    """部署异常时尽量删除服务器临时压缩包。"""
    subprocess.run(
        ["ssh", SSH_HOST, f"rm -f -- {shlex.quote(remote_archive)}"],
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL,
        check=False,
    )


def main() -> int:
    package_manager = detect_package_manager()
    seven_zip = command_path(SEVEN_ZIP)
    command_path("ssh")
    command_path("scp")

    archive_name = f"{ARCHIVE_PREFIX}-{uuid.uuid4().hex}.7z"
    archive_path = Path(tempfile.gettempdir()) / archive_name
    remote_archive = f"/tmp/{archive_name}"
    uploaded = False

    try:
        build_project(package_manager)
        create_archive(archive_path, seven_zip)

        print(f"正在上传到服务器 {SSH_HOST}……")
        run(["scp", str(archive_path), f"{SSH_HOST}:{remote_archive}"])
        uploaded = True

        deploy_remote(remote_archive)

        print("\n前端部署成功。")
        return 0

    except (RuntimeError, subprocess.CalledProcessError) as error:
        print(f"\n前端部署失败：{error}", file=sys.stderr)
        if uploaded:
            cleanup_remote_archive(remote_archive)
        return 1

    finally:
        if archive_path.exists():
            archive_path.unlink()


if __name__ == "__main__":
    raise SystemExit(main())
