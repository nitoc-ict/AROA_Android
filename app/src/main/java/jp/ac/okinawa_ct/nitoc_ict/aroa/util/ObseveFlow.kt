package jp.ac.okinawa_ct.nitoc_ict.aroa.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * [Flow] のcollectが簡単に出来る [AppCompatActivity] 拡張関数
 * [AppCompatActivity.lifecycleScope] 内で、
 * 引数で渡された flow の collect に
 * 引数で渡された collector を渡す
 */
fun <T> AppCompatActivity.observeFlow(flow: Flow<T>, collector: (T) -> Unit) {
    lifecycleScope.launch {
        flow.collect {
            collector(it)
        }
    }
}

/**
 * [Flow] のcollectが簡単に出来る [Fragment] 拡張関数
 * [Fragment.lifecycleScope] 内で、
 * 引数で渡された flow の collect に
 * 引数で渡された collector を渡す
 */
fun <T> Fragment.observeFlow(flow: Flow<T>, collector: (T) -> Unit) {
    lifecycleScope.launch {
        flow.collect(collector)
    }
}