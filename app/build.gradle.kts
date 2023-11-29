plugins {
    id("com.android.application")

    // Google 서비스 Gradle 플러그인 추가
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.broaf"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.broaf"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures.viewBinding = true
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("androidx.preference:preference:1.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-analytics")
//    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database:20.3.0")

    //카카오맵 SDK에의 의존성
    implementation ("com.kakao.maps.open:android:2.6.0")

    implementation("androidx.recyclerview:recyclerview:1.2.1")
}