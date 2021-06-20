import org.jetbrains.kotlin.gradle.plugin.PLUGIN_CLASSPATH_CONFIGURATION_NAME

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

dependencies {
    add(PLUGIN_CLASSPATH_CONFIGURATION_NAME, libs.compose.compiler)
    implementation(project(":shared"))
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.exoplayer)
    implementation(libs.youtubeExtractor) {
        exclude("com.android.support", "support-annotations")
    }
    implementation(libs.paging.compose)
    implementation(libs.firebase.dynamicLinks)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.accompanist)
}

val isCI = System.getenv("CI") == "true"
println("Is CI environment: $isCI")

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "com.popalay.barnee"
        minSdk = 28
        targetSdk = 30
        versionCode = properties.getOrDefault("barnee.versioncode", 1).toString().toInt()
        versionName = "1.0.5"

        signingConfigs {
            getByName("debug") {
                storeFile = file("../release/debug.keystore")
            }
            register("release") {
                storeFile = file("../release/release.keystore")
                keyAlias = "barnee"
                storePassword = System.getenv("ANDROID_RELEASE_KEYSTORE_PWD").orEmpty()
                keyPassword = System.getenv("ANDROID_RELEASE_KEY_PWD").orEmpty()
            }
        }

        buildTypes {
            getByName("debug") {
                signingConfig = signingConfigs.getByName("debug")
                versionNameSuffix = "-dev"
                applicationIdSuffix = ".debug"
            }

            getByName("release") {
                signingConfig = if (isCI) signingConfigs.getByName("release") else signingConfigs.getByName("debug")
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }

        lint {
            isCheckReleaseBuilds = false
            isCheckDependencies = true
            isIgnoreTestSources = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}
