package com.unity3d.player

fun UnityPlayerActivity.setTrialNameText(trialName: String) {
    UnityPlayer.UnitySendMessage("Android", "SetTrialName", trialName)
}

fun UnityPlayerActivity.setRecordTimeText(time: String) {
    UnityPlayer.UnitySendMessage("Android", "SetRecordTime", time)
}

fun UnityPlayerActivity.setCurrentRankText(rank: String) {
    UnityPlayer.UnitySendMessage("Android", "SetCurrentRank", rank)
}

fun UnityPlayerActivity.setCirclesState(state: String) {
    UnityPlayer.UnitySendMessage("Android", "SetCirclesState", state)
}

fun UnityPlayerActivity.setAlertText(text: String) {
    UnityPlayer.UnitySendMessage("Android", "SetAlertText", text)
}