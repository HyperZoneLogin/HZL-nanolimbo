import org.gradle.jvm.tasks.Jar

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinLombok)
}

group = "icu.h2l.login"
// 作为主仓库 submodule 构建时沿用主仓库版本；独立构建时使用 pluginVersion。
if (findProject(":api") == null) {
    version = providers.gradleProperty("pluginVersion").orElse("1.0.0-SNAPSHOT").get()
}

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
    maven("https://maven.yuluo.dev/repository/maven-public/")
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
    // Keep the default version so the standalone plugin jar carries the module
    // version (HZL-nanolimbo-<version>.jar / HZL-backend-nanolimbo-<version>.jar).
    archiveClassifier.set("")
}

// 独立构建时也要把 velocity-plugin.json 里的版本占位符替换成实际版本。
tasks.withType<ProcessResources>().configureEach {
    val pluginVersion = project.version.toString()
    inputs.property("pluginVersion", pluginVersion)
    filteringCharset = "UTF-8"

    val srcTemplate = project.file("src/main/resources/velocity-plugin.json")
    if (srcTemplate.exists()) {
        inputs.file(srcTemplate)
        exclude("velocity-plugin.json")
        doLast("injectPluginVersion") {
            val dst = destinationDir.resolve("velocity-plugin.json")
            dst.parentFile.mkdirs()
            dst.writeText(srcTemplate.readText().replace("\${pluginVersion}", pluginVersion))
        }
    }
}
