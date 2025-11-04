plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("kotlin-parcelize")
}

android {
    namespace = "me.alexandervortex.shelfie"
    compileSdk = 36

    defaultConfig {
        applicationId = "me.alexandervortex.shelfie"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.1"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    // Settings
    implementation(libs.androidx.datastore.preferences)

    // Media
    implementation(libs.androidx.media)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.icons)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.nav.compose)

    // Navigation
    implementation(libs.nav.compose)

    // Parsers
    implementation(libs.jsoup)
    implementation(libs.serialization.core)
    implementation(libs.storage)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.framework)
    testImplementation(libs.mockk)
}