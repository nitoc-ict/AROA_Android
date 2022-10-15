package com.unity3d.player

class UnitySendMesageUtil {
    fun UnityPlayerActivity.setTrialNameText(trialName: String) {
        UnityPlayer.UnitySendMessage("Android", "SetTrialName", trialName)
    }
    fun UnityPlayerActivity.setRecordTimeText(time: String) {
        UnityPlayer.UnitySendMessage("Android", "SetRecordTime", time)
    }
    fun UnityPlayerActivity.setCurrentRankText(rank: String) {
        UnityPlayer.UnitySendMessage("Android", "SetCurrentRank", rank)
    }
    fun UnityPlayerActivity.setCircleRadius(radius: String) {
        UnityPlayer.UnitySendMessage("Android", "SetCircleRadius", radius)
    }
}