plugins {
    // Defines the version for the Android Application plugin (uses AGP version from TOML)
    alias(libs.plugins.android.application) apply false

    // Defines the version for the Kotlin Android plugin (uses Kotlin version from TOML)
    alias(libs.plugins.kotlin.android) apply false

    // Defines the version for the Compose Compiler plugin (uses Kotlin version from TOML)
    alias(libs.plugins.kotlin.compose) apply false

    // CRITICAL FIX: Defines the KSP version (2.0.0-1.0.21) using the explicit version variable from TOML.
    // This aligns KSP with your Kotlin 2.0.21 compiler.
    id("com.google.devtools.ksp") version libs.versions.kspVersion.get() apply false
}
