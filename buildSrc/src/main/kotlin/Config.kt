@file:Suppress("ClassName")

object config {
    const val namespace = "org.skyfaced.noti"
    const val applicationId = "org.skyfaced.noti"
    const val compileSdk = 34
    const val minSdk = 21
    const val targetSdk = 34

    object version {
        /** @see <a href="https://developer.android.com/studio/publish/versioning">Look at warning section of android developer docs</a> */
        private const val VERSION_CODE_MAX = 2100000000

        private const val major = 1
        private const val minor = 0
        private const val patch = 1

        val code = (major * 10000 + minor * 100 + patch).apply { require(this < VERSION_CODE_MAX) }
        const val name = "$major.$minor.$patch"
    }

    sealed class variant(
        val name: String,
        val versionNameSuffix: String = "",
        val applicationIdSuffix: String = "",
        val applicationName: String = defaultAppName,
    ) {
        object debug : variant("debug", "-dev", ".dev", "NOTI Dev")

        object release : variant("release")

        companion object {
            const val appNameKey = "appNameKey"
            const val defaultAppName = "NOTI"
            const val defaultAppNameLowercase = "noti"

            /**
             * Don't forget to add each new variant
             */
            val all by lazy(LazyThreadSafetyMode.NONE) { listOf(debug, release) }
        }
    }
}