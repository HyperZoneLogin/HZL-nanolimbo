import org.gradle.jvm.tasks.Jar

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinLombok)
}

group = "icu.h2l.login"
version = providers.gradleProperty("pluginVersion").orElse("1.0.0-SNAPSHOT").get()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

sourceSets {
    main {
        // 直接编译仓库内 vendored 的 NanoLimbo 源码（与上游 NanoLimbo 仓库保持一致）。
        java.srcDir("NanoLimbo/src/main/java")
        resources.srcDir("NanoLimbo/src/main/resources")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.opencollab.dev/maven-snapshots")
    maven("https://maven.elytrium.net/repo/")
    maven {
        name = "velocityctdSnapshots"
        url = uri("https://repo.velocityctd.com/snapshots")
    }
}

val hzlApiVersion = providers.gradleProperty("hzlApiVersion").orElse("1.0.0-SNAPSHOT").get()

dependencies {
    // 在主仓库内被 include 时直接用本地 :api；独立构建时用已发布的 HZL API。
    if (findProject(":api") != null) {
        compileOnly(project(":api"))
    } else {
        compileOnly("icu.h2l.login:api:$hzlApiVersion")
    }
    compileOnly(libs.velocityApi)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.configurateYaml)
    compileOnly(libs.nettyAll)
    compileOnly(libs.adventureApi)
    compileOnly(libs.adventureTextSerializerGson)
    compileOnly(libs.adventureTextSerializerLegacy)
    compileOnly(libs.adventureTextSerializerJsonLegacyImpl)
    compileOnly(libs.adventureTextSerializerPlain)
    compileOnly(libs.adventureTextMinimessage)
    compileOnly(libs.adventureNbt)
    compileOnly(libs.gson)
}

tasks.named<Jar>("jar") {
    archiveClassifier.set("")
    archiveVersion.set("")
}
