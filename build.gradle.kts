plugins {
    java
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

/**
 * Artefact Version
 */
val artefactVersion: String by project

group = "com.ronglankj"
version = artefactVersion

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}


dependencies {
    // **************
    // * 依赖版本定义
    // **************
    val mybatisFlexVersion: String by project
    val devkitVersion: String by project
    val auth0JwtVersion: String by project

    implementation("com.onixbyte:devkit-utils:$devkitVersion")
    implementation("com.onixbyte:guid:$devkitVersion")
    implementation("com.auth0:java-jwt:$auth0JwtVersion")
    implementation("com.onixbyte:simple-jwt-facade:$devkitVersion")
    implementation("com.onixbyte:simple-jwt-authzero:$devkitVersion")
    implementation("com.onixbyte:simple-jwt-spring-boot-starter:$devkitVersion")
    implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter:$mybatisFlexVersion")
    implementation("com.zaxxer:HikariCP")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("io.projectreactor:reactor-test")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
