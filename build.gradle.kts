import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    id("jacoco")
}

group = "cn.nagico"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

springBoot {
    mainClass.set("cn.nagico.teamup.backend.ApplicationKt")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven {
        setUrl("https://mirrors.163.com/maven/repository/maven-public/")
        content {
            includeGroup("io.sentry")
        }
    }
    maven {
        setUrl("https://maven.aliyun.com/repository/public")
    }
    maven {
        setUrl("https://maven.aliyun.com/repository/spring")
    }
    maven {
        setUrl("https://maven.aliyun.com/repository/spring-plugin")
    }
    maven {
        setUrl("https://maven.aliyun.com/repository/gradle-plugin")
    }
    mavenCentral()
}

dependencies {
    testImplementation("org.mockito:mockito-core:3.6.28")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.powermock:powermock-core:2.0.9")
    testImplementation("org.powermock:powermock-reflect:2.0.9")
    testImplementation("org.powermock:powermock-api-mockito2:2.0.9") {
        exclude(group = "org.objenesis", module = "objenesis")
        exclude(group = "org.mockito", module = "mockito-core")
    }

    // ws
    implementation("io.netty:netty-all:4.1.91.Final")

    // jwt
    implementation("com.nimbusds:nimbus-jose-jwt:9.31")

    // spring
    implementation("org.springframework.boot:spring-boot-starter")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // json
    implementation("com.alibaba:fastjson:2.0.15")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")

    // hutool
    implementation("cn.hutool:hutool-all:5.8.11")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.redisson:redisson-spring-boot-starter:3.20.1")

    // rabbitmq
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("com.github.fridujo:rabbitmq-mock:1.2.0")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

val excludePatterns = setOf(
    "cn/nagico/teamup/backend/stomp/protocol/*",
    "cn/nagico/teamup/backend/stomp/exception/**",
    "cn/nagico/teamup/backend/Application.kt",
)

tasks.withType<JacocoCoverageVerification> {
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it).apply {
                exclude(excludePatterns)
            }
        }))
    }
}

tasks.withType<JacocoReport> {
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it).apply {
                exclude(excludePatterns)
            }
        }))
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("$buildDir/reports/html"))
    }
}

jacoco {
    toolVersion = "0.8.8"
    reportsDirectory.set(layout.buildDirectory.dir("$buildDir/reports/"))
}


// 拷贝项目依赖
tasks.register("copyLib") {
    doLast {
        println(configurations.runtimeClasspath)

        delete("$buildDir/libs/lib")

        copy {
            from(configurations.runtimeClasspath)
            into("$buildDir/libs/lib")
        }
    }
}

//支持启动类
tasks.bootJar {
    enabled = true
    exclude("*.jar")

    // 指定依赖包的路径
    manifest {
        attributes(
            "Manifest-Version" to 1.0,
            "Class-Path" to configurations.runtimeClasspath.get().files.joinToString(" ") {
                "lib/${it.name}"
            }
        )
    }

    finalizedBy("copyLib")
}

buildscript {
    dependencies {
        classpath("org.yaml:snakeyaml:1.17")
        classpath("com.fasterxml.jackson.core:jackson-core:2.13.4")
    }
}
