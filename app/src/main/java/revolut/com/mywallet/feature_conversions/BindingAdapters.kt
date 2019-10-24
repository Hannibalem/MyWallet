package revolut.com.mywallet.feature_conversions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import revolut.com.mywallet.utils.showKeyboard

object BindingAdapters {

    @BindingAdapter("conversions")
    @JvmStatic
    fun RecyclerView.bindConversions(models: List<ConversionViewModel>) {
        if (adapter == null) {
            layoutManager = LinearLayoutManager(context)
            adapter = ConversionsAdapter(context, models)
        } else {
            (adapter as? ConversionsAdapter)?.apply {
                setViewModels(models)
            }
        }
    }

    @BindingAdapter("hasFocus")
    @JvmStatic
    fun EditText.bindHasFocus(hasFocus: Boolean) {
        if (hasFocus) {
            isEnabled = true
            requestFocus()
            showKeyboard()
        } else {
            isEnabled = false
        }
    }

    @BindingAdapter("focusListener")
    @JvmStatic
    fun EditText.bindFocusListener(listener: (Boolean) -> Unit) {
        onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus -> listener(hasFocus) }
    }

    @BindingAdapter("textListener")
    @JvmStatic
    fun EditText.bindTextListener(listener: (String) -> Unit) {
        var watcher = tag as? TextWatcher
        watcher?.let { removeTextChangedListener(it) }
        watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { listener(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        addTextChangedListener(watcher)
        tag = watcher
    }

    @BindingAdapter("visible")
    @JvmStatic
    fun View.bindVisible(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }
}
