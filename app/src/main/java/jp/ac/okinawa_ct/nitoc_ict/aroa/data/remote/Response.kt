package jp.ac.okinawa_ct.nitoc_ict.aroa.data.remote

/**
 * ネットワークレスポンスを表現するクラス
 *
 * @property isSuccess  この[Response]が成功しているのかを表します
 * @property isError    この[Response]が失敗しているのかを表します
 */
sealed class Response<out R> {
    val isSuccess:  Boolean get() = this is Success
    val isError:    Boolean get() = this is Error

    /**
     * 成功を表現するクラス
     *
     * @property data 取得したデータ
     */
    data class Success<out T>(val data: T) : Response<T>()

    /**
     * 失敗を表現するクラス
     *
     * @property isRemoteError  リモート側のエラーか？
     * @property isClientError  クライアント側のエラーか？
     * @property exception      投げられた例外
     */
    data class Error(
        val isRemoteError: Boolean,
        val isClientError: Boolean,
        val exception: Exception,
    ) : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data:$data]"
            is Error -> "Error[exception:$exception]"
        }
    }
}
