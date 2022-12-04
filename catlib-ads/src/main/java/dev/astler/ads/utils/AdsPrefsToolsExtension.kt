package dev.astler.ads.utils

import dev.astler.catlib.preferences.PreferencesTool

fun PreferencesTool.isRewardAdIsActive(): Boolean = getBoolean("rewardAdActiveKey", false)
fun PreferencesTool.setRewardAdIsActive() = edit("rewardAdActiveKey", true)
fun PreferencesTool.unsetRewardAdIsActive() = edit("rewardAdActiveKey", false)
