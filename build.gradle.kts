plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.aboutlibraries) apply false
}

buildscript {
    dependencies {
        classpath(libs.navigationSafeArgs)
        classpath(libs.googleServices)
        classpath(libs.firebaseCrashlytics)
        classpath(libs.daggerHiltGradle)
    }
}
