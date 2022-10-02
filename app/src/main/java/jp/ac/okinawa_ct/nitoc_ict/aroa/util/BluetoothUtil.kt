package jp.ac.okinawa_ct.nitoc_ict.aroa.util

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import jp.ac.okinawa_ct.nitoc_ict.aroa.R

/**
 * BLUETOOTH_SCAN のパーミッションがあるか確認する [Fragment]の拡張関数
 */
fun Fragment.checkBluetoothScanPermission(): Boolean {
    val permission = ContextCompat.checkSelfPermission(
        requireContext(),
        BLUETOOTH_CONNECT
    )
    return permission == PackageManager.PERMISSION_GRANTED
}

/**
 * BLUETOOTH_SCAN のパーミッションを取得するメソッド
 * このパーミッションが必要な理由について説明するAlertDialogを出した後にパーミッションのリクエストをする
 * 引数では、リクエスト後の処理を渡せる
 */
fun Fragment.buildRequestBluetoothScanPermissionAlertDialog(
    callback: (isPermissionGranted: Boolean) -> Unit = {}
) : AlertDialog {
    val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        callback(isGranted)
    }

    return AlertDialog
        .Builder(requireContext())
        .setMessage(R.string.reason_of_using_bluetooth)
        .setPositiveButton(R.string.action_next) { _, _ ->
            launcher.launch(BLUETOOTH_CONNECT)
        }
        .setNegativeButton(R.string.action_cancel) { _, _ ->
            callback(false)
        }
        .setOnCancelListener {
            callback(false)
        }
        .create()
}