package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord.record_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.ListItemRecordBinding
import jp.ac.okinawa_ct.nitoc_ict.aroa.util.TimeFormat

typealias OnItemClickListener = (view: View, position: Int) -> Unit

class RecordListAdapter(
    context: Context
) : ListAdapter<Record, RecordListAdapter.BindingHolder>(ITEM_CALLBACK) {
    private val inflater = LayoutInflater.from(context)

    private var onItemClickListener: OnItemClickListener? = null

    class BindingHolder(
        val binding: ListItemRecordBinding
    ) : RecyclerView.ViewHolder(binding.root)
    companion object {
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<Record>() {
            override fun areItemsTheSame(
                oldItem: Record,
                newItem: Record
            ): Boolean = oldItem.userId == newItem.userId

            override fun areContentsTheSame(
                oldItem: Record,
                newItem: Record
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = ListItemRecordBinding.inflate(inflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(bindingHolder: BindingHolder, position: Int) {
        val current = getItem(position)
        when(current) {
            is Record.MarathonRecord -> {
                bindingHolder.binding.recordTrialName.text = current.trialName
                bindingHolder.binding.recordTrialDistance.text = current.distance.toString() + "m"
                bindingHolder.binding.recordTrialTime.text = TimeFormat().convertLongToTimeString(current.time)
            }
            else -> {}
        }

        bindingHolder.binding.recordTrialName.setOnClickListener {
            onItemClickListener?.invoke(it,position)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}