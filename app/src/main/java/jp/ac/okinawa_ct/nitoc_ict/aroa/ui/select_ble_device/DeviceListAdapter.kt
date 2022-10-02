package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.select_ble_device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.ItemBluetoothDeviceBinding

typealias OnItemClickListner = (view: View, position: Int) -> Unit

@SuppressLint("MissingPermission")
class DeviceListAdapter(
    context: Context
) : ListAdapter<BluetoothDevice, DeviceListAdapter.BindingHolder>(ITEM_CALLBACK) {

    private val inflater = LayoutInflater.from(context)

    private var onItemClickListener: OnItemClickListner? = null

    class BindingHolder(
        val binding: ItemBluetoothDeviceBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<BluetoothDevice>() {
            override fun areItemsTheSame(
                oldItem: BluetoothDevice,
                newItem: BluetoothDevice
            ): Boolean = oldItem.address == newItem.address

            override fun areContentsTheSame(
                oldItem: BluetoothDevice,
                newItem: BluetoothDevice
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = ItemBluetoothDeviceBinding.inflate(inflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(bindingHolder: BindingHolder, position: Int) {
        val current = getItem(position)
        bindingHolder.binding.deviceName.text = current.name
        bindingHolder.binding.root.setOnClickListener {
            onItemClickListener?.invoke(it, position)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListner) {
        this.onItemClickListener = onItemClickListener

    }
}