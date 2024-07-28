plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin)
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

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    namespace = "dev.astler.catlib.signin"
}

dependencies {
    implementation(project(":catlib-core"))
    implementation(project(":catlib-ui"))

    implementation(libs.credentials)
    implementation(libs.credentialsPlayServicesAuth)
    implementation(libs.googleid)

    api(platform(libs.firebaseBom))
    api(libs.firebaseAuthKtx)
    api(libs.playServicesAuth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espressoCore)

    api(libs.hiltAndroid)
    kapt("com.google.dagger:hilt-compiler:${libs.versions.hilt.get()}")
    api(libs.hiltNavigationFragment)
}
