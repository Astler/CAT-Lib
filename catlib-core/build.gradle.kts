plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    id("maven-publish")
}

project.afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
            }
        }
    }
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 19
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    kotlin {
        jvmToolchain(19)
    }

    kotlinOptions {
        jvmTarget = "19"
    }

    kapt {
        correctErrorTypes = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }

    namespace = "dev.astler.catlib.core"
}

dependencies {
    debugApi("com.squareup.leakcanary:leakcanary-android:2.14")

    api("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    api("com.google.android.gms:play-services-base:18.4.0")
    api("com.google.android.gms:play-services-tasks:18.1.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    api("com.google.guava:guava:32.0.1-jre")
    api("com.google.android.play:review-ktx:2.0.1")
    api("androidx.appcompat:appcompat:${libs.versions.appcompat.get()}")
    api("androidx.appcompat:appcompat-resources:${libs.versions.appcompat.get()}")
    api("androidx.emoji2:emoji2:1.4.0")
    api("androidx.core:core-ktx:1.13.1")
    api("androidx.activity:activity-ktx:1.9.0")
    api("androidx.fragment:fragment-ktx:1.7.1")
    api("androidx.multidex:multidex:2.0.1")
    api("androidx.legacy:legacy-support-v4:1.0.0")
    api("androidx.preference:preference-ktx:1.2.1")
    api("androidx.vectordrawable:vectordrawable:1.2.0")
    api("androidx.work:work-runtime-ktx:2.9.0")
    api("androidx.recyclerview:recyclerview:1.3.2")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    api("androidx.lifecycle:lifecycle-common-java8:2.8.0")
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api("com.google.android.material:material:1.12.0")
    api("com.google.android.play:feature-delivery-ktx:2.1.0")
    api("com.google.android.play:app-update-ktx:2.1.0")
    api("androidx.navigation:navigation-fragment-ktx:2.7.7")
    api("androidx.navigation:navigation-ui-ktx:2.7.7")
    api("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")
    api("io.coil-kt:coil:${libs.versions.coil.get()}")
    api("io.coil-kt:coil-gif:${libs.versions.coil.get()}")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    api(platform("com.google.firebase:firebase-bom:33.0.0"))
    api("com.google.firebase:firebase-analytics-ktx")
    api("com.google.firebase:firebase-crashlytics-ktx")
    api("com.google.firebase:firebase-config-ktx")
    api("com.google.firebase:firebase-inappmessaging-display-ktx")
    api("com.google.firebase:firebase-messaging-ktx")
    api("com.google.firebase:firebase-perf-ktx")
    api("androidx.startup:startup-runtime:1.2.0-alpha02")
    api("com.google.code.gson:gson:2.10.1")
    api("com.devtodev:android-google:1.0.0")
    api("com.devtodev:android-analytics:2.3.5")
    api("com.android.installreferrer:installreferrer:2.2")

    implementation("com.jakewharton.timber:timber:5.0.1")

    api("com.google.dagger:hilt-android:${libs.versions.hilt.get()}")
    kapt("com.google.dagger:hilt-compiler:${libs.versions.hilt.get()}")
    api("androidx.hilt:hilt-navigation-fragment:1.2.0")
}
