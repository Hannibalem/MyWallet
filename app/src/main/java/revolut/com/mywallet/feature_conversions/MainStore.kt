package revolut.com.mywallet.feature_conversions

import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import revolut.com.mywallet.utils.store.BaseStore
import revolut.com.mywallet.domain.IGetConversionsUseCase
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.utils.store.State
import revolut.com.mywallet.utils.store.Store

class MainStore(private val getConversionsUseCase: IGetConversionsUseCase): BaseStore<MainState>(MainState.NONE), IMainStore {

    private var cachedConversions: List<Conversion> = emptyList()
    private var conversionsDisposable: Disposable? = null

    override fun getConversions(base: Conversion) {
        conversionsDisposable?.dispose()
        conversionsDisposable = buildState({
            getConversionsUseCase
                .reorderConversions(base, cachedConversions).flatMapCompletable { rates ->
                    Completable.fromAction {
                        transformState { state -> state.copy(status = State.Status.LOADING, model = state.model.copy(base = base, conversions = rates)) }
                    }
                }
                .andThen(getConversionsUseCase.getConversionTable(base.currency))
                .map { table ->
                    val allConversions = mutableListOf(Conversion(base.currency, 1.0)).apply { addAll(table.conversions) }
                    ConversionModel(base, allConversions.map { Conversion(it.currency, it.value * base.value) })
                }
                .doOnNext { model ->
                    cachedConversions = model.conversions
                    transformState { state -> state.copy(status = State.Status.READY, model = model) }
                }
                .ignoreElements()
        }, {
            Completable.complete()
        })
    }
}

interface IMainStore: Store<MainState> {

    fun getConversions(base: Conversion)
}
