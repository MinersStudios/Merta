import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    alias(libs.plugins.android.application)
}

dependencies {
    implementation(project(":lib"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.splashscreen)
    implementation(libs.fastutil)
    implementation(libs.flexbox)
    implementation(libs.material)
}

android {
    defaultConfig {
        namespace   = "com.minersstudios.genero"
        versionName = "1.0.0"
        versionCode = 1
        compileSdk  = 34
        minSdk      = 28
        targetSdk   = 34

        resourceConfigurations.addAll(
            listOf("en", "uk", "ru")
        )
    }

    buildTypes {
        release {
            isDebuggable      = false
            isMinifyEnabled   = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}
