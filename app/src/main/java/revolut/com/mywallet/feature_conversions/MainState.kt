package revolut.com.mywallet.feature_conversions

import revolut.com.mywallet.utils.store.State
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.Currency

data class MainState(val status: State.Status, val model: ConversionModel):
    State {

    companion object {
        val NONE = MainState(
            State.Status.NONE,
            ConversionModel(Conversion(Currency.EUR, 1.0), emptyList())
        )
    }
}
