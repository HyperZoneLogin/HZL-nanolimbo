# HZL-nanolimbo

HyperZoneLogin 的可选内置 NanoLimbo 后端模块，独立于主仓库
[HyperzoneLogin](https://github.com/HyperZoneLogin/HyperzoneLogin) 维护与发布。

- `src/main/kotlin`: HZL 的 `HyperSubModule` 包装层与命令。
- `NanoLimbo/`: vendored 的 [NanoLimbo](https://github.com/HyperZoneLogin/NanoLimbo) 源码。
- 产物：`build/libs/HZL-nanolimbo-<version>.jar`，带完整的 `velocity-plugin.json`，
  可直接作为独立 Velocity 插件载入（依赖主插件 `hyperzonelogin`）。

## 构建

需要先让本地 Maven 拥有 HZL 核心 API：

```bash
# 在主仓库
./gradlew :api:publishToMavenLocal

# 在本仓库
./gradlew build
```

API 版本通过 `gradle.properties` 的 `hzlApiVersion` 控制（默认 `1.0.0-SNAPSHOT`）。

## 安装

把构建出的 jar 和主插件 `HyperZoneLogin.jar` 一起放进 Velocity 的 `plugins/` 目录即可。
本模块在 `ProxyInitializeEvent` 中把 `NanoLimboSubModule` 注册到主插件，不会与主插件内置模块重复加载。

## CI

`.github/workflows/release.yml` 会：

1. 克隆主仓库并把 `:api` 发布到 `mavenLocal`；
2. 构建本模块；
3. 在推送 tag 时创建 GitHub Release 并上传 jar。

主仓库的周发布工作流可通过 `repository_dispatch`（`hzl-release` / `hzl-weekly`）
触发本仓库同步发布。
