package dev.astler.unlib_ads.utils

import dev.astler.unlib.PreferencesTool

fun PreferencesTool.isRewardAdIsActive(): Boolean = getBoolean("rewardAdActiveKey", false)
fun PreferencesTool.setRewardAdIsActive() = edit("rewardAdActiveKey", true)
fun PreferencesTool.unsetRewardAdIsActive() = edit("rewardAdActiveKey", false)
