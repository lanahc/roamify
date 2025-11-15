plugins {
    // Essential Android plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Kotlin Compose Compiler plugin
    alias(libs.plugins.kotlin.compose)

    // CRITICAL: KSP plugin for Room code generation
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.roamify"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.roamify"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // We now use the standard aliases for all dependencies instead of val variables for versions.

    // --- MVVM and Room Dependencies ---
    // Note: Room runtime and KTX dependencies will use the versions defined in the TOML file.

    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // CRITICAL: Room Annotation Processor (must be 'ksp')
    ksp("androidx.room:room-compiler:2.6.1")

    // Lifecycle components (ViewModel and LiveData)
    implementation(libs.androidx.lifecycle.runtime.ktx) // Use existing TOML entry

    // The following KTX dependencies are often missing from the TOML list you provided,
    // so we use string literals with stable versions.
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Compose Integration Fixes (Needed for observeAsState and viewModel())
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // --- Existing Dependencies (Using Version Catalog) ---

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Test dependencies
    testImplementation(libs.junit) // Alias for JUnit 4
    androidTestImplementation(libs.androidx.junit) // Alias for androidx.test.ext:junit
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // FIX: Using the correct alias for Compose JUnit testing
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}