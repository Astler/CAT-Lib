plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose)
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
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    kotlin {
        jvmToolchain(19)
    }

    kotlinOptions {
        jvmTarget = "19"
    }

    publishing {
        singleVariant("release")
    }

    kapt {
        correctErrorTypes = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }

    namespace = "dev.astler.ui"
}

dependencies {
    implementation(project(":catlib-core"))
    implementation(libs.aboutlibrariesComposeM3)

    api(libs.accompanistSystemuicontroller)
    api(libs.material3)
    api(libs.material3Android)
    api(libs.material3WindowSizeClass)

    api("androidx.compose.ui:ui-tooling:${libs.versions.compose.get()}")
    api("androidx.compose.ui:ui-util:${libs.versions.compose.get()}")
    api("androidx.compose.ui:ui-tooling-preview:${libs.versions.compose.get()}")
    api("androidx.compose.ui:ui:${libs.versions.compose.get()}")
    api("androidx.compose.runtime:runtime:${libs.versions.compose.get()}")
    api(libs.androidxMaterial)
    api(libs.animation)
    api(libs.activityCompose)
    api(libs.navigationCompose)
    api(libs.materialIconsExtended)
    api(libs.runtimeLivedata)
    api(libs.kotlinxMetadataJvm)

    api(libs.foundation)
    api(libs.accompanistInsets)

    api(libs.hiltNavigationCompose)
    api(libs.lifecycleViewmodelCompose)
    api(libs.lifecycleRuntimeCompose)

    api(libs.composeShimmer)
    api(libs.constraintlayoutCompose)
    api(libs.konfettiCompose)

    api(libs.coilCompose)
    api(libs.transformations)

    api(libs.coreSplashscreen)
    api(libs.shimmer)
    api(libs.transformations)
    api(libs.hiltAndroid)
    kapt("com.google.dagger:hilt-compiler:${libs.versions.hilt.get()}")
    api(libs.hiltNavigationFragment)
    api(libs.insetter)

    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espressoCore)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoInline)
    testImplementation(libs.coreTesting)
}
