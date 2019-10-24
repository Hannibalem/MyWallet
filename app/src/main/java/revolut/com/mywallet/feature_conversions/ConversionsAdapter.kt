package revolut.com.mywallet.feature_conversions

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import revolut.com.mywallet.R
import revolut.com.mywallet.utils.recyclerview.RecyclerViewDataBindingAdapter
import revolut.com.mywallet.utils.recyclerview.RecyclerViewDataBindingViewHolder
import revolut.com.mywallet.databinding.ConversionLayoutBinding

class ConversionsAdapter(context: Context, models: List<ConversionViewModel>):
    RecyclerViewDataBindingAdapter<ConversionViewModel, List<ConversionViewModel>, ConversionLayoutBinding, CurrencyViewHolder>
        (context, models) {

    override fun setViewModels(models: List<ConversionViewModel>) {
        val diffCallback = DiffUtilCallback(this.models, models)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        super.setViewModels(models)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getLayout() = R.layout.conversion_layout

    override fun getViewHolder(binding: ConversionLayoutBinding) =
        CurrencyViewHolder(binding)

    override fun bindItem(itemBinding: ConversionLayoutBinding, model: ConversionViewModel) {
        itemBinding.viewModel = model
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.binding.viewModel?.value?.set(payloads[0] as String)
            return
        }
        onBindViewHolder(holder, position)
    }

    private class DiffUtilCallback(private val oldList: List<ConversionViewModel>, private val newList: List<ConversionViewModel>): DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition].name == newList[newPosition].name
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            if (oldList[oldPosition].hasFocus.get()) {
                return true
            }
            return oldList[oldPosition].value.get()?.toDouble() == newList[newPosition].value.get()?.toDouble()
        }

        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
            return newList[newPosition].value.get()
        }
    }
}

class CurrencyViewHolder(binding: ConversionLayoutBinding):
    RecyclerViewDataBindingViewHolder<ConversionLayoutBinding>(binding)
