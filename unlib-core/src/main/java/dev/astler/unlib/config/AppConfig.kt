package dev.astler.unlib.config

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val mMainActivityPackage: String = ""
)