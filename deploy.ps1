# deploy.ps1
# 保存为 UTF-8 with BOM
$server = "heimenkyou@luowb.cn"
$remotePath = "/opt/club"
$localJar = "target/club-recruitment-0.0.1-SNAPSHOT.jar"
$localYml = "src/main/resources/application-prod.yml"
$tempJar = "target/club.jar"

Write-Host "🚀 开始打包项目..." -ForegroundColor Green

mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0)
{
    Write-Error "❌ Maven 打包失败！"
    exit 1
}

Write-Host "✅ 打包完成"

Copy-Item $localJar $tempJar -Force
Write-Host "📦 已重命名 jar 为 club.jar"

Write-Host "📤 正在上传文件到服务器 ${server}:${remotePath} ..." -ForegroundColor Yellow

scp $tempJar "${server}:${remotePath}"
if ($LASTEXITCODE -ne 0)
{
    Write-Error "❌ 上传 JAR 失败！"
    exit 1
}

scp $localYml "${server}:${remotePath}"
if ($LASTEXITCODE -ne 0)
{
    Write-Error "❌ 上传 YML 失败！"
    exit 1
}

Write-Host "✅ 文件上传成功"

Write-Host "🔄 正在重启远程服务..."
ssh "${server}" "systemctl restart club.service"

Write-Host "🎉 部署完成！"

$seeLog = Read-Host "是否查看实时日志？(y/n)"
if ($seeLog -eq "y" -or $seeLog -eq "Y")
{
    ssh "${server}" "journalctl -u club.service -f"
}