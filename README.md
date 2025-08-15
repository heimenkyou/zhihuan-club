# 社团网站后端

这是社团网站的后端服务，最初用于社团招新，当前提供以下功能：

- 留言板系统（可留言、点赞、防刷赞）
- 报名社团功能

未来会拓展更多功能。

## 环境要求

- JDK 17+
- Maven 3+
- MySQL 数据库
- Redis 7.4.2

## 开发启动

1. 克隆代码：

```bash
git clone <你的仓库地址>
cd club-recruitment
````

2. 配置数据库：

* 将 `application-dev模板.yml` 复制一份为 `application-dev.yml`：

```bash
cp src/main/resources/application-dev模板.yml src/main/resources/application-dev.yml
```

* 修改 `application-dev.yml` 中的数据库相关信息。

3. 启动项目：

* **通过 IDE（如 IntelliJ IDEA）开发**

    * 将启动项配置文件选择为 `dev`
    * 或在启动参数里添加 JVM 参数：`-Dspring.profiles.active=dev`

* **打包后运行**：

```bash
mvn clean package -DskipTests
java -jar target/club-recruitment-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=dev
```

4. 访问接口：

* API 根路径：`http://localhost:12321`
* 示例接口：`GET /test`

## 后续扩展

* 前端页面可部署在同域名下，与后端共享 `club.luowb.cn`
* 更多社团管理功能、用户系统、文件上传等

## 联系方式

* 作者: \[heimenkyou]
* Email: \[Wenbin.Lo@outlook.com]