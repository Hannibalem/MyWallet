package revolut.com.mywallet.feature_conversions

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.utils.databinding.KtObservableField
import revolut.com.mywallet.utils.store.State

class MainViewModel(private val store: IMainStore) {

    var selectedConversion: Conversion? = null

    private var storeDisposable: Disposable? = null

    val conversions: KtObservableField<List<ConversionViewModel>> = KtObservableField(emptyList())

    val loading: KtObservableField<Boolean> = KtObservableField(true)

    fun onStart() {
        storeDisposable = store.subscribe(
            Consumer {
                conversions.set(it.model.conversions.map { item -> ConversionViewModel(store, item, item.currency == it.model.base.currency ) })
                loading.set(it.status == State.Status.LOADING && it.model.conversions.size <= 1)
                if (selectedConversion == null) {
                    store.getConversions(it.model.base)
                }
                selectedConversion = it.model.base
            }
        )
        selectedConversion?.let { store.getConversions(it) }
    }

    fun onStop() {
        storeDisposable?.dispose()
        store.stop()
    }
}
