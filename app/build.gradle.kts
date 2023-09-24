import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    kotlin("android")
}

@Suppress("UnstableApiUsage")
android {
    namespace = config.namespace

    compileSdk = config.compileSdk

    defaultConfig {
        applicationId = config.applicationId
        minSdk = config.minSdk
        targetSdk = config.targetSdk
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
        create(config.variant.release.name) {
            keyAlias = getLocalProperty("alias")
            keyPassword = getLocalProperty("key")
            storeFile = file(getLocalProperty("path"))
            storePassword = getLocalProperty("store")
        }
    }

    buildTypes {
        getByName(config.variant.debug.name) {
            buildConfigField("String", "APP_NAME", config.variant.debug.applicationName.escaped())
            archivesName = "${config.variant.defaultAppNameLowercase}-${config.version.name}"

            isDebuggable = true
            versionNameSuffix = config.variant.debug.versionNameSuffix
            applicationIdSuffix = config.variant.debug.applicationIdSuffix
        }

        getByName(config.variant.release.name) {
            buildConfigField("String", "APP_NAME", config.variant.release.applicationName.escaped())
            archivesName = "${config.variant.defaultAppNameLowercase}-${config.version.name}"

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
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/${name}/kotlin")
            }
        }

        val applicationName = when (buildType.name) {
            config.variant.debug.name -> config.variant.debug.applicationName
            config.variant.release.name -> config.variant.release.applicationName
            else -> throw IllegalArgumentException("Unrecognized build type: ${buildType.name}")
        }

        mergedFlavor.manifestPlaceholders[config.variant.appNameKey] = applicationName
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // UI
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("cafe.adriel.lyricist:lyricist:1.4.2")
    ksp("cafe.adriel.lyricist:lyricist-processor:1.4.2")

    // Compose
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.compose.foundation:foundation:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.2")

    // Database
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")

    // Util
    implementation("com.squareup.logcat:logcat:0.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}