import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    alias(libs.plugins.android.library)
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.fastutil)
    implementation(libs.jetbrains.annotations)
    implementation(libs.jsr305)
    implementation(libs.material)
}

android {
    defaultConfig {
        namespace  = "com.minersstudios.genero.lib"
        compileSdk = 34
        minSdk     = 28

        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }
}
