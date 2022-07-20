plugins {
    id("com.android.application") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
    id("com.google.devtools.ksp") version "1.7.0-1.0.6" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}