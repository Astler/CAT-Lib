package dev.astler.ads.utils

import dev.astler.catlib.preferences.PreferencesTool

private const val ChildAdsKey = "child_ads"
private const val AgeConfirmedKey = "age_confirmed"
private const val LastAdsTimeKey = "last_ads_time"
private const val RewardAdActiveKey = "reward_ad_active_key"

var PreferencesTool.rewardAdActive: Boolean
    get() = getBoolean(RewardAdActiveKey, false)
    set(value) {
        edit(RewardAdActiveKey, value)
    }


var PreferencesTool.childAdsMode: Boolean
    get() = getBoolean(ChildAdsKey, false)
    set(value) {
        edit(ChildAdsKey, value)
    }

var PreferencesTool.ageConfirmed: Boolean
    get() = getBoolean(AgeConfirmedKey, false)
    set(value) {
        edit(AgeConfirmedKey, value)
    }


var PreferencesTool.lastAdsTime: Long
    get() = getLong(LastAdsTimeKey, 0L)
    set(value) {
        edit(LastAdsTimeKey, value)
    }
