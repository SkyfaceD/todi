@file:Suppress("ClassName")

object config {
    object version {
        /** @see <a href="https://developer.android.com/studio/publish/versioning">Look at warning section of android developer docs</a> */
        private const val VERSION_CODE_MAX = 2100000000

        private const val major = 1
        private const val minor = 0
        private const val patch = 0

        val code = (major * 10000 + minor * 100 + patch).apply { require(this < VERSION_CODE_MAX) }
        const val name = "$major.$minor.$patch"
    }
}