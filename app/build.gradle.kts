plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)

    alias(libs.plugins.jetbrains.kotlin.serialization)

}

android {
    namespace = "com.example.aistudyplanner"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.aistudyplanner"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.ai)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.lifecycle.viewmodel.navigation3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.androidx.foundation)
    implementation(libs.material3)


// For lifecycleScope in Activity and Fragment
    implementation(libs.androidx.lifecycle.runtime.ktx.v280)
// or latest
// (Optional) ViewModel & LiveData if you're using them
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx.v292)
// or latestimplementation(libs.kotlinx.serialization.core)
//  implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation3.runtime.v100alpha07)
    implementation(libs.androidx.navigation3.ui.v100alpha07)

    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)


    implementation(libs.lottie.compose) // Latest stable

    implementation(libs.androidx.material.icons.core)


    implementation(libs.androidx.material3.v140alpha14)

    // transcribe

    implementation(libs.okhttp)
    implementation(libs.json)
    implementation(libs.jsoup)

    implementation(libs.tom.roush.pdfbox.android)



}