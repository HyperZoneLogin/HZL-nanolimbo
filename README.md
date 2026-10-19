# HZL-nanolimbo

HyperZoneLogin 的可选内置 NanoLimbo 后端模块，独立于主仓库
[HyperzoneLogin](https://github.com/HyperZoneLogin/HyperzoneLogin) 维护与发布。

- `src/main/kotlin`: HZL 的 `HyperSubModule` 包装层与命令。
- `NanoLimbo/`: vendored 的 [NanoLimbo](https://github.com/HyperZoneLogin/NanoLimbo) 源码。
- 产物：`build/libs/HZL-nanolimbo-<version>.jar`（HZL 嵌入式模块，无独立 `velocity-plugin.json`）。

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

把构建出的 jar 放进主插件的可选模块目录，并在 `core.conf` 中启用
`modules.backend-nanolimbo`。主仓库默认不打包本模块。

## CI

`.github/workflows/release.yml` 会：

1. 克隆主仓库并把 `:api` 发布到 `mavenLocal`；
2. 构建本模块；
3. 在推送 tag 时创建 GitHub Release 并上传 jar。

主仓库的周发布工作流可通过 `repository_dispatch`（`hzl-release` / `hzl-weekly`）
触发本仓库同步发布。
