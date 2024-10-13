plugins {
    alias(libs.plugins.android.application)

    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.shop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shop"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // các đơn vị

    implementation("com.intuit.ssp:ssp-android:1.1.0")

    implementation("com.intuit.sdp:sdp-android:1.1.0")

    // image

    implementation("com.makeramen:roundedimageview:2.3.0")

    // firebase

    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))

    implementation ("com.google.firebase:firebase-firestore:25.1.0")

}