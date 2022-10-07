package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.checkrecord.record_ranking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Record
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.ListItemRecordRankingBinding

typealias OnItemClickListener = (view: View, position: Int) -> Unit

class RecordRankingListAdapter(
    context: Context
) : ListAdapter<Record, RecordRankingListAdapter.BindingHolder>(ITEM_CALLBACK) {
    private val inflater = LayoutInflater.from(context)

    private var onItemClickListener: OnItemClickListener? = null

    class BindingHolder(
        val binding: ListItemRecordRankingBinding
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
        val binding = ListItemRecordRankingBinding.inflate(inflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(bindingHolder: BindingHolder, position: Int) {
        val current = getItem(position)
        when(current) {
            is Record.MarathonRecord -> {
                bindingHolder.binding.rankNum.text = current.rank.toString()
                bindingHolder.binding.userName.text = current.userId
                bindingHolder.binding.time.text = current.time.toString()}
            else -> {}
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}