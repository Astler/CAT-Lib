# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.devtodev.huawei.Huawei
-dontwarn com.devtodev.huawei.HuaweiToken

-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}