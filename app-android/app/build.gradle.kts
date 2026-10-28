plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.turabazaar.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.turabazaar.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.1.0"
    }

    signingConfigs {
        create("release") {
            val keystoreFile = System.getenv("TURABAZAAR_KEYSTORE_PATH") ?: "keystore/turabazaar.jks"
            storeFile = file(keystoreFile)
            storePassword = System.getenv("TURABAZAAR_KEYSTORE_PASSWORD") ?: "turabazaar-dev"
            keyAlias = System.getenv("TURABAZAAR_KEY_ALIAS") ?: "turabazaar"
            keyPassword = System.getenv("TURABAZAAR_KEY_PASSWORD") ?: "turabazaar-dev"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { viewBinding = true }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
        // Firebase BoM – Versionen zentral verwaltet
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
}
