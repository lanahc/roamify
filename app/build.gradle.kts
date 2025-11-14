plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // 1. ADD THE KSP PLUGIN HERE (This is required for Room's annotation processing)
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
        // Set Java 11 compatibility if your project requires it, otherwise use Java 17 (recommended)
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
    // Define versions for clarity and easier updates
    // --- UPDATED VERSIONS ---
    val roomVersion = "2.8.3"        // Updated from 2.6.1
    val lifecycleVersion = "2.9.4"  // Updated from 2.7.0
    val coroutinesVersion = "1.10.2" // Updated from 1.7.3

    // --- MVVM and Room Dependencies ---

    // Room components
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Coroutines support
    ksp("androidx.room:room-compiler:$roomVersion") // COMPILER: Use ksp, NOT kapt

    // Lifecycle components (ViewModel and LiveData)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // ** NEW: Compose LiveData Integration **
    implementation("androidx.compose.runtime:runtime-livedata")

    // ** NEW: Compose ViewModel Integration (The fix for your current error) **
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // --- Existing Dependencies ---

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}