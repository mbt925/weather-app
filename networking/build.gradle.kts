@file:Suppress("UnstableApiUsage")
@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlinx.serializationPlugin)
}

android {
    namespace = "com.mbt925.weatherapp.networking"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(libs.squareup.retrofit.library)
    implementation(libs.squareup.okhttp3.loggingInterceptor)
    api(libs.kotlinx.serialization.json)
    api(libs.squareup.retrofit.converter.kotlinxSerialization)
    implementation(libs.koin.core)

    testImplementation(project(":core:test"))
}
