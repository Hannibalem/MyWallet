package revolut.com.mywallet.utils.databinding

import androidx.databinding.Observable
import androidx.databinding.ObservableField

class KtObservableField<T : Any?>(value: T, vararg dependencies: Observable) : ObservableField<T>(*dependencies) {

    init {
        set(value)
    }

    override fun set(value: T) {
        if (get() != value) {
            super.set(value)
        }
    }
}
