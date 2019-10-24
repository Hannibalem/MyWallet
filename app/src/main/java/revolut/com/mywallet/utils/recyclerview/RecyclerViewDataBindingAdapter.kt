package revolut.com.mywallet.utils.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewDataBindingAdapter<M, L: List<M>, B: ViewDataBinding, VH: RecyclerViewDataBindingViewHolder<B>>(
    private val context: Context,
    protected var models: L
) : RecyclerView.Adapter<VH>() {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return getViewHolder(DataBindingUtil.inflate<B>(LayoutInflater.from(context), getLayout(), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindItem(holder.binding, models[position])
        holder.binding.executePendingBindings()
    }

    final override fun getItemCount() = models.size

    open fun setViewModels(models: L) {
        this.models = models
    }

    abstract fun getLayout(): Int

    abstract fun getViewHolder(binding: B): VH

    abstract fun bindItem(itemBinding: B, model: M)
}
