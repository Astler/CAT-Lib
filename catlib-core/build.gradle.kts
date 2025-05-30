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
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true
    }

    publishing {
        singleVariant("release")
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
    implementation(libs.aboutlibrariesCore)
    api(project(":resources"))

    debugApi(libs.leakcanaryAndroid)

    api(libs.kotlinReflect)
    api(libs.playServicesBase)
    api(libs.playServicesTasks)
    api(libs.kotlinxSerializationJson)
    api(libs.kotlinxCoroutinesCore)
    api(libs.kotlinxCoroutinesAndroid)
    api(libs.guava)
    api(libs.reviewKtx)
    api(libs.appcompat)
    api(libs.appcompatResources)
    api(libs.emoji2)
    api(libs.coreKtx)
    api(libs.activityKtx)
    api(libs.fragmentKtx)
    api(libs.multidex)
    api(libs.legacySupportV4)
    api(libs.preferenceKtx)
    api(libs.vectordrawable)
    api(libs.workRuntimeKtx)
    api(libs.recyclerview)
    api(libs.constraintlayout)
    api(libs.lifecycleLivedataKtx)
    api(libs.lifecycleViewmodelKtx)
    api(libs.lifecycleCommonJava8)
    api(libs.lifecycleExtensions)
    api(libs.material)
    api(libs.featureDeliveryKtx)
    api(libs.appUpdateKtx)
    api(libs.navigationFragmentKtx)
    api(libs.navigationUiKtx)
    api(libs.navigationDynamicFeaturesFragment)
    api(libs.coil)
    api(libs.coilGif)
    api(libs.coilNetworkOkhttp)

    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espressoCore)

    api(platform(libs.firebaseBom))
    api(libs.firebaseAnalyticsKtx)
    api(libs.firebaseCrashlyticsKtx)
    api(libs.firebaseConfigKtx)
    api(libs.firebaseInappmessagingDisplayKtx)
    api(libs.firebaseMessagingKtx)
    api(libs.firebasePerfKtx)
    api(libs.startupRuntime)
    api(libs.gson)
    api(libs.androidGoogle)
    api(libs.androidAnalytics)
    api(libs.installreferrer)

    implementation(libs.timber)

    api(libs.hiltAndroid)
    kapt("com.google.dagger:hilt-compiler:${libs.versions.hilt.get()}")
    api(libs.hiltNavigationFragment)
}
