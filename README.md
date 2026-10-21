# BaseApp

[![](https://jitpack.io/v/HuaiJunHu/BaseApp.svg)](https://jitpack.io/#HuaiJunHu/BaseApp)

一个基于 Android 的基础开发框架，使用 Kotlin 构建，旨在简化 Android 应用开发。

## 功能特性

- Kotlin 开发
- Android 基础组件封装
- 通用工具类
- 可扩展的项目基础架构

> 请根据项目实际实现补充或删减功能，避免将尚未实现的能力列为项目特性。

## 环境要求

- Android Studio
- Kotlin
- Gradle
- Android SDK

## 安装

### 1. 添加 JitPack 仓库

在项目根目录的 `settings.gradle.kts` 文件中添加：

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://jitpack.io")
        }
    }
}
```

如果项目使用 Groovy DSL，即 `settings.gradle`，则配置为：

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()

        maven {
            url 'https://jitpack.io'
        }
    }
}
```

### 2. 添加项目依赖

在应用模块的 `app/build.gradle.kts` 中添加：

```kotlin
dependencies {
    implementation("com.github.HuaiJunHu:BaseApp:版本号")
}
```

将 `版本号` 替换为 JitPack 页面中实际构建成功的 Release Tag。

如果尚未创建 Release，也可以使用具体的 Git commit hash 或分支快照版本，具体规则参见 [JitPack 官方文档](https://docs.jitpack.io/)。

## 使用方法

请根据项目实际公开的 API 添加示例：

```kotlin
// 在这里添加 BaseApp 的实际使用代码
```

## 版本信息

查看 [JitPack 构建与版本页面](https://jitpack.io/#HuaiJunHu/BaseApp)。

## 问题反馈

如果遇到问题或有功能建议，请前往 [GitHub 仓库](https://github.com/HuaiJunHu/BaseApp) 提交 Issue。

## License

请以项目仓库中实际声明的开源许可证为准。
