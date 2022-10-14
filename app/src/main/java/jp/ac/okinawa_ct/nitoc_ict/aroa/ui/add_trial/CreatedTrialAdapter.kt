package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.add_trial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.dto.Trial
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.ListItemTrialBinding

typealias OnItemClickListener = (view: View, position: Int) -> Unit

class CreatedTrialAdapter(
    context: Context
) : ListAdapter<Trial, CreatedTrialAdapter.BindingHolder>(ITEM_CALLBACK) {
    private val inflater = LayoutInflater.from(context)

    private var onItemClickListener: OnItemClickListener? = null

    class BindingHolder(
        val binding: ListItemTrialBinding
    ) : RecyclerView.ViewHolder(binding.root)
    companion object {
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<Trial>() {
            override fun areItemsTheSame(
                oldItem: Trial,
                newItem: Trial
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Trial,
                newItem: Trial
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {

        val binding = ListItemTrialBinding.inflate(inflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(bindingHolder: BindingHolder, position: Int) {
        val current = getItem(position)
        bindingHolder.binding.trialName.text = current.name
        bindingHolder.binding.trialMakeDate.text = current.createDate
        bindingHolder.binding.itemViewGroup.setOnClickListener {
            onItemClickListener?.invoke(it,position)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}

