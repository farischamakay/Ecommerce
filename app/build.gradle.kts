plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.ecommerce"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.ecommerce"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //splashscreen API
    implementation("androidx.core:core-splashscreen:1.0.1")

    //fragment
    implementation("androidx.fragment:fragment-ktx:1.6.1")

    //Navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")

    //SharedPreference
    implementation ("androidx.preference:preference-ktx:1.2.1")

    //ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //Paging3
    implementation("androidx.paging:paging-runtime-ktx:3.2.0")

    //Room
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    //Swipe Refresh Layout
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Shimmer Effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //Chucker
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")

    //DaggerHilt
    implementation ("com.google.dagger:hilt-android:2.45")
    kapt ("com.google.dagger:hilt-android-compiler:2.45")
}