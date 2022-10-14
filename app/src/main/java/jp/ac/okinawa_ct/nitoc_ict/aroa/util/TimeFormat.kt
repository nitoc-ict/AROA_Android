package jp.ac.okinawa_ct.nitoc_ict.aroa.util

import android.annotation.SuppressLint
import android.text.format.DateUtils

class TimeFormat {
    @SuppressLint("SimpleDateFormat")
    fun convertLongToTimeString(time: Long): String {
//        val hours = time / 3600
//        val minutes = time % 3600 / 60
//        val seconds = time % 3600 % 60
//        val formatTime = hours.toString() + ":" + minutes.toString() + ":" + seconds.toString()
        return DateUtils.formatElapsedTime(time)
    }


}