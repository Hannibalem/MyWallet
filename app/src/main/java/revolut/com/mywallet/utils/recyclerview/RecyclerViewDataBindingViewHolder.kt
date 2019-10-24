package revolut.com.mywallet.utils.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class RecyclerViewDataBindingViewHolder<out B: ViewDataBinding>(val binding: B)
    : RecyclerView.ViewHolder(binding.root)
