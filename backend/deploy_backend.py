#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Spring Boot 后端部署脚本。

当前配置：
- SSH 主机：nat-249
- 远端目录：/opt/services/club
- systemd 服务：club.service
- 远端 JAR：app.jar
- 远端配置：application-prod.yml

流程：Maven 打包 -> 上传 JAR 和配置 -> 自动创建/修正远端目录 ->
备份旧版本 -> 替换文件 -> sudo 免密重启 systemd -> 失败回滚。
"""

from __future__ import annotations

import shlex
import shutil
import subprocess
import sys
import uuid
from pathlib import Path


# =============================================================================
# 配置区域：新项目通常只改这三项
# =============================================================================

# ~/.ssh/config 中的主机别名。
SSH_HOST = "nat-249"

# 服务器上的后端目录。
REMOTE_DIR = "/opt/services/club"

# systemd 服务名，写不写 .service 都可以。
SERVICE_NAME = "club"


# =============================================================================
# 当前项目固定配置
# =============================================================================

REMOTE_JAR_NAME = "app.jar"
REMOTE_CONFIG_NAME = "application-prod.yml"
DEPLOY_USER = "heimenkyou"
DEPLOY_GROUP = "heimenkyou"
KEEP_BACKUPS = 5
SKIP_TESTS = True


# =============================================================================
# 高级配置：项目结构特殊时再改
# =============================================================================

# None：自动选择 target 中体积最大的可部署 JAR。
LOCAL_JAR: Path | None = None

# None：自动寻找 application-prod.yml 或 application-prod.yaml。
LOCAL_CONFIG: Path | None = None


# 通过 SSH 直接交给服务器 bash 执行，不需要在服务器保存此脚本。
REMOTE_SCRIPT = r"""
set -Eeuo pipefail

remote_dir="$1"
uploaded_jar="$2"
uploaded_config="$3"
jar_name="$4"
config_name="$5"
service="$6"
keep="$7"
deploy_user="$8"
deploy_group="$9"

fail() {
    echo "部署失败：$*" >&2
    exit 1
}

[[ "$remote_dir" == /opt/services/* ]] ||
    fail "远端目录不在允许范围内：$remote_dir"
[[ "$uploaded_jar" == /tmp/*.jar ]] ||
    fail "临时 JAR 路径不合法"
[[ "$uploaded_config" == /tmp/* ]] ||
    fail "临时配置路径不合法"
[[ -f "$uploaded_jar" ]] ||
    fail "服务器上没有找到上传的 JAR"
[[ -f "$uploaded_config" ]] ||
    fail "服务器上没有找到上传的配置文件"
[[ "$jar_name" != */* && -n "$jar_name" ]] ||
    fail "JAR 文件名不合法"
[[ "$config_name" != */* && -n "$config_name" ]] ||
    fail "配置文件名不合法"
[[ "$service" =~ ^[A-Za-z0-9_.@-]+\.service$ ]] ||
    fail "systemd 服务名不合法"
[[ "$keep" =~ ^[1-9][0-9]*$ ]] ||
    fail "备份数量必须是正整数"

# 首次部署自动创建目录；以后每次顺便保证目录归属正确。
sudo -n /usr/bin/install \
    -d \
    -o "$deploy_user" \
    -g "$deploy_group" \
    -m 0755 \
    "$remote_dir"

backup_dir="$remote_dir/.deploy-backups"
mkdir -p "$backup_dir"

timestamp="$(date +%Y%m%d-%H%M%S-%N)"
backup_file="$backup_dir/$timestamp.tar.gz"

backup_items=()
has_backup=false
new_files_installed=false
finished=false

restore_backup() {
    echo "正在恢复上一版本……" >&2

    rm -f \
        "$remote_dir/$jar_name" \
        "$remote_dir/$config_name"

    if [[ "$has_backup" == true ]]; then
        tar -C "$remote_dir" -xzf "$backup_file"
        sudo -n /usr/bin/systemctl restart "$service" || true
    fi
}

cleanup() {
    status=$?

    rm -f "$uploaded_jar" "$uploaded_config"

    # 替换文件后出现异常时自动恢复。
    if [[ "$finished" != true && "$new_files_installed" == true ]]; then
        restore_backup
    fi

    exit "$status"
}
trap cleanup EXIT

if [[ -f "$remote_dir/$jar_name" ]]; then
    backup_items+=("$jar_name")
fi

if [[ -f "$remote_dir/$config_name" ]]; then
    backup_items+=("$config_name")
fi

if ((${#backup_items[@]} > 0)); then
    tar -C "$remote_dir" \
        -czf "$backup_file" \
        "${backup_items[@]}"

    has_backup=true
    echo "旧版本已备份：$backup_file"
fi

# 先写临时文件，再通过 mv 替换正式文件。
install \
    -m 0644 \
    "$uploaded_jar" \
    "$remote_dir/$jar_name.new-$timestamp"

install \
    -m 0640 \
    "$uploaded_config" \
    "$remote_dir/$config_name.new-$timestamp"

mv -f \
    "$remote_dir/$jar_name.new-$timestamp" \
    "$remote_dir/$jar_name"

mv -f \
    "$remote_dir/$config_name.new-$timestamp" \
    "$remote_dir/$config_name"

new_files_installed=true

echo "正在重启服务：$service"
sudo -n /usr/bin/systemctl restart "$service"

# 等待 Spring Boot 完成最初启动。
sleep 3

if ! /usr/bin/systemctl is-active --quiet "$service"; then
    fail "新版本启动后没有保持运行"
fi

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

echo "后端部署完成：$remote_dir/$jar_name"
"""


def run(
    command: list[str],
    *,
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
        input=input_bytes,
        check=True,
    )

def command_path(command: str) -> str:
    """取得命令实际路径，找不到时直接报错。"""
    path = shutil.which(command)
    if path is None:
        raise RuntimeError(f"没有找到命令：{command}")
    return path


def normalize_service_name(service: str) -> str:
    """统一转换为完整的 .service 名称。"""
    return service if service.endswith(".service") else f"{service}.service"


def build_project() -> None:
    """优先使用 Maven Wrapper，否则使用系统 Maven。"""
    print("开始执行 Maven 打包……")

    arguments = ["clean", "package"]
    if SKIP_TESTS:
        arguments.append("-DskipTests")

    if Path("mvnw.cmd").is_file():
        command = [str(Path("mvnw.cmd").resolve()), *arguments]
    elif Path("mvnw").is_file():
        command = [command_path("bash"), "mvnw", *arguments]
    else:
        command = [command_path("mvn"), *arguments]

    run(command)


def resolve_jar() -> Path:
    """寻找最终用于部署的 Spring Boot JAR。"""
    if LOCAL_JAR is not None:
        jar = LOCAL_JAR.resolve()
        if not jar.is_file():
            raise RuntimeError(f"指定的 JAR 不存在：{jar}")
        return jar

    target_dir = Path("target")
    if not target_dir.is_dir():
        raise RuntimeError("没有找到 target 目录")

    candidates = [
        path
        for path in target_dir.glob("*.jar")
        if not path.name.startswith("original-")
        and not path.name.endswith("-sources.jar")
        and not path.name.endswith("-javadoc.jar")
        and not path.name.endswith("-tests.jar")
    ]

    if not candidates:
        raise RuntimeError("target 目录中没有找到可部署的 JAR")

    # Spring Boot 可执行 JAR 通常体积最大。
    candidates.sort(key=lambda path: path.stat().st_size, reverse=True)

    if len(candidates) > 1:
        print(f"检测到多个 JAR，选择体积最大的文件：{candidates[0].name}")

    return candidates[0].resolve()


def resolve_config() -> Path:
    """寻找生产环境配置文件。"""
    if LOCAL_CONFIG is not None:
        config = LOCAL_CONFIG.resolve()
        if not config.is_file():
            raise RuntimeError(f"指定的配置文件不存在：{config}")
        return config

    for config in (
        Path("src/main/resources/application-prod.yml"),
        Path("src/main/resources/application-prod.yaml"),
    ):
        if config.is_file():
            return config.resolve()

    raise RuntimeError(
        "没有找到 application-prod.yml 或 application-prod.yaml，"
        "路径特殊时请修改 LOCAL_CONFIG"
    )


def deploy_remote(remote_jar: str, remote_config: str, service: str) -> None:
    """在服务器端创建目录、备份、替换文件并重启服务。"""
    arguments = [
        shlex.quote(REMOTE_DIR),
        shlex.quote(remote_jar),
        shlex.quote(remote_config),
        shlex.quote(REMOTE_JAR_NAME),
        shlex.quote(REMOTE_CONFIG_NAME),
        shlex.quote(service),
        str(KEEP_BACKUPS),
        shlex.quote(DEPLOY_USER),
        shlex.quote(DEPLOY_GROUP),
    ]
    remote_command = "bash -s -- " + " ".join(arguments)

    print("正在服务器端部署并重启服务……")
    run(["ssh", SSH_HOST, remote_command], input_text=REMOTE_SCRIPT)


def cleanup_remote_files(*remote_files: str) -> None:
    """部署异常时尽量删除服务器临时文件。"""
    files = " ".join(shlex.quote(path) for path in remote_files)
    subprocess.run(
        ["ssh", SSH_HOST, f"rm -f -- {files}"],
        stdout=subprocess.DEVNULL,
        stderr=subprocess.DEVNULL,
        check=False,
    )


def main() -> int:
    command_path("ssh")
    command_path("scp")

    service = normalize_service_name(SERVICE_NAME)
    deploy_id = uuid.uuid4().hex
    remote_jar = f"/tmp/spring-{deploy_id}.jar"
    remote_config = f"/tmp/spring-{deploy_id}.config"

    uploaded_jar = False
    uploaded_config = False

    try:
        build_project()

        jar_file = resolve_jar()
        config_file = resolve_config()

        print(f"使用 JAR：{jar_file}")
        print(f"使用配置：{config_file}")
        print(f"远端 JAR：{REMOTE_DIR}/{REMOTE_JAR_NAME}")
        print(f"远端配置：{REMOTE_DIR}/{REMOTE_CONFIG_NAME}")
        print(f"systemd 服务：{service}")

        print("正在上传 JAR……")
        run(["scp", str(jar_file), f"{SSH_HOST}:{remote_jar}"])
        uploaded_jar = True

        print("正在上传生产配置……")
        run(["scp", str(config_file), f"{SSH_HOST}:{remote_config}"])
        uploaded_config = True

        deploy_remote(remote_jar, remote_config, service)

        print("\n后端部署成功。")
        return 0

    except (RuntimeError, subprocess.CalledProcessError) as error:
        print(f"\n后端部署失败：{error}", file=sys.stderr)
        if uploaded_jar or uploaded_config:
            cleanup_remote_files(remote_jar, remote_config)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
