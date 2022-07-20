plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    kotlin("android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "org.skyfaced.todi"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            versionNameSuffix = "-dev"
            applicationIdSuffix = ".dev"
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "${projectDir}/proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    packagingOptions {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/${name}/kotlin")
            }
        }
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0")
    implementation("androidx.activity:activity-compose:1.5.0")

    // UI
    implementation("androidx.compose.material3:material3:1.0.0-alpha14")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.13-rc")

    // Compose
    implementation("androidx.compose.ui:ui:1.2.0-rc03")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.0-rc03")
    implementation("androidx.compose.foundation:foundation:1.2.0-rc03")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.0-rc03")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.5.0")

    // Database
    implementation("androidx.room:room-runtime:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")
    ksp("androidx.room:room-compiler:2.4.2")

    // Util
    implementation("com.squareup.logcat:logcat:0.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}