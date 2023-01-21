@file:Suppress("UnstableApiUsage")
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

android {
    namespace = "com.mbt925.weatherapp.feature"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }
}

dependencies {
    api(project(":feature:api"))
    api(project(":feature:ui"))
    implementation(project(":service"))
    implementation(project(":networking"))
    implementation(project(":design"))

    implementation(libs.android.gps.location)
    implementation(libs.koin.android)
    implementation(libs.androidx.activity.compose)

    testImplementation(project(":core:test"))
}
