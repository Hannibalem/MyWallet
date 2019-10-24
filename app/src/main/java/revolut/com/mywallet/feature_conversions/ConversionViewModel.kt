package revolut.com.mywallet.feature_conversions

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import revolut.com.mywallet.entities.Conversion
import java.math.BigDecimal
import java.math.RoundingMode

class ConversionViewModel(
    private val store: IMainStore,
    private val conversion: Conversion,
    inEditMode: Boolean
) {
    val hasFocus = ObservableBoolean(inEditMode)

    val name = conversion.currency.name

    val value = ObservableField<String>(transformValue(conversion.value))

    private fun transformValue(exchange: Double): String {
        val roundedExchange = BigDecimal.valueOf(exchange).setScale(2, RoundingMode.HALF_UP).toDouble()
        return if (roundedExchange % 1 == 0.0) {
            String.format("%.0f", exchange)
        } else {
            String.format("%.2f", exchange)
        }
    }

    val onTextChangedListener: (String) -> Unit = {
        if (it != value.get()) {
            value.set(it)
            getConversions()
        }
    }

    val onFocusListener: (Boolean) -> Unit = {
        hasFocus.set(it)
    }

    fun onClick() {
        hasFocus.set(true)
        getConversions()
    }

    private fun getConversions() {
        val amount: Double = value.get()?.let { if (it.isNotEmpty()) it.toDoubleOrNull() else 0.0 } ?: 0.0
        store.getConversions(Conversion(conversion.currency, amount))
    }
}
