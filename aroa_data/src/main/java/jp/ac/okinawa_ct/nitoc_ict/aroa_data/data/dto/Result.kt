package jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto

/**
 * 取得に失敗する可能性のあるデータを表現するクラス
 *
 * @property isLoading  この[Result]がローディング中かを表します
 * @property isSuccess  この[Result]が成功しているのかを表します
 * @property isError    この[Result]が失敗しているのかを表します
 */
@Suppress("MemberVisibilityCanBePrivate")
sealed class Result<out R> {
    val isLoading:  Boolean get() = this is Loading
    val isSuccess:  Boolean get() = this is Success
    val isError:    Boolean get() = this is Error

    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val isNetworkError: Boolean,
        val exception: Exception,
        ) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data:$data]"
            is Error -> "Error[exception:$exception]"
            is Loading -> "Loading..."
        }
    }
}
