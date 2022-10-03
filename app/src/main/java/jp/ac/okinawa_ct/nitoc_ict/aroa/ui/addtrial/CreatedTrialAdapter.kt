package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.addtrial

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
        bindingHolder.binding.itemViewGroup.setOnClickListener {
            onItemClickListener?.invoke(it,position)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}

//class CreatedTrialAdapter: ListAdapter<Trial, CreatedTrialAdapter.ViewHolder>(CreatedTrialDiffCallback()) {
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    class ViewHolder private constructor(private val binding: ListItemTrialBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: Trial) {
//            binding.trialName.text = item.name
////            binding.executePendingBindings()
//        }
//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ListItemTrialBinding.inflate(layoutInflater,parent,false)
//                return ViewHolder(binding)
//            }
//        }
//    }
//}
//
//class CreatedTrialDiffCallback : DiffUtil.ItemCallback<Trial>() {
//    override fun areItemsTheSame(oldItem: Trial, newItem: Trial): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Trial, newItem: Trial): Boolean {
//        return oldItem == newItem
//    }
//}

