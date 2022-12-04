package dev.astler.ads.utils

import dev.astler.catlib.preferences.PreferencesTool

fun PreferencesTool.setRewardAdIsActive() = edit("rewardAdActiveKey", true)
fun PreferencesTool.unsetRewardAdIsActive() = edit("rewardAdActiveKey", false)
