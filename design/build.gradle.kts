@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

android {
    namespace = "com.mbt925.weatherapp.design"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
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

    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.navigation)
    api(libs.koin.androidx.compose)
    implementation(libs.androidx.compose.material.windowSizeClass)
    api(libs.androidx.compose.uiToolingPreview)
    debugApi(libs.androidx.compose.uiTooling)
}
