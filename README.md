# 公途公考服务端

## 开发环境

- Amazon Corretto OpenJDK 21
- Spring Boot 3.3.3
- OnixByte DevKit 1.6.2
- Postgres 16.0

## 构建工具 (Gradle)

1. 若在下载 Gradle Wrapper 这一步特别缓慢，可选择开启代理或将 `./gradle/wrapper/gradle-wrapper.properties` 中的 `distributionUrl` 修改为腾讯云托管的 Gradle 镜像地址。
2. 若在下载依赖时缓慢，可以在 `build.gradle.kts` 的 `repositories` 修改为下面的代码：
   ```kotlin
   repositories {
       mavenLocal() // 添加本地 Maven 仓库，若依赖已存在于本地仓库中则无需下载
       maven("https://maven.proxy.ustclug.org/maven2/") // 由中国科学技术大学提供的反代镜像
       maven("https://maven.aliyun.com/repository/public") // 由阿里云提供的 maven 镜像
       mavenCentral() // Maven 中央仓库镜像
   }
   ```
3. Gradle 开启了 Wrapper 模式，为了避免因 Gradle 版本不兼容导致的构建问题，**建议**使用 `gradlew` 命令替换 `gradle` 命令。
