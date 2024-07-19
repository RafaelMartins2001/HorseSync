import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.compose.ui.tooling.preview) // Preview tools for Compose UI.
            implementation(libs.androidx.activity.compose) // Compose integration for Android activities.
            implementation(libs.ktor.client.android) // Ktor client for Android.
            implementation(libs.koin.android) // Koin dependency injection for Android.
            implementation(libs.coil.android) // Coil image loading library for Android.
            implementation(libs.coil.core)
            implementation("androidx.navigation:navigation-compose:2.5.3")
            implementation("androidx.compose.ui:ui:1.3.0")
            implementation("androidx.compose.material:material:1.3.0")
            implementation("androidx.compose.ui:ui-tooling-preview:1.3.0")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.shared)
            implementation(libs.kmmViewModel) // Kotlin Multiplatform Mobile ViewModel support.
            implementation(libs.multiplatform.settings.no.arg) // Multiplatform settings library without argument support.
            implementation(libs.kotlinx.coroutines) // Kotlin Coroutines for asynchronous programming.
            implementation(libs.bundles.ktor.common) // Common Ktor dependencies for HTTP client/server.
            implementation(compose.material3) // Material 3 design components for Compose UI.
            implementation(libs.koin.core) // Koin core library for dependency injection.
            implementation(libs.koin.compose) // Koin integration with Compose for dependency injection.
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
            implementation ("io.github.wojciechosak:calendar:1.0.1")
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
dependencies {
    implementation(libs.androidx.foundation.android)
}

