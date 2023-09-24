plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    kotlin("android")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "org.skyfaced.noti"

    compileSdk = 33

    defaultConfig {
        applicationId = "org.skyfaced.noti"
        minSdk = 21
        targetSdk = 33
        versionCode = config.version.code
        versionName = config.version.name

        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        bundle {
            language {
                enableSplit = false
            }
        }
        resourceConfigurations.addAll(listOf("en", "ru"))
    }

    signingConfigs {
        create("release") {
            keyAlias = getLocalProperty("alias")
            keyPassword = getLocalProperty("key")
            storeFile = file(getLocalProperty("path"))
            storePassword = getLocalProperty("store")
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
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
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
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
    implementation("androidx.activity:activity-compose:1.6.1")

    // UI
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")

    // Compose
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
    implementation("androidx.compose.foundation:foundation:1.3.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.3")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Database
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    ksp("androidx.room:room-compiler:2.5.0")

    // Util
    implementation("com.squareup.logcat:logcat:0.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}