package revolut.com.mywallet.utils.store

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

abstract class BaseStore <S : State>(initialState: S) :
    Store<S> {

    private val stateHolder: Subject<S> = BehaviorSubject.createDefault(initialState).toSerialized()
    private val stateTransformer: StateTransformer<S> =
        StateTransformer(stateHolder)
    private val compositeDisposable = CompositeDisposable()

    override fun subscribe(consumer: Consumer<S>): Disposable {
        return stateHolder.observeOn(Schedulers.computation())
            .subscribe(consumer)
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }

    protected fun buildState(buildStateFunction: () -> Completable, buildErrorStateFunction: (Throwable) -> Completable): Disposable =
        buildStateFunction()
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext { buildErrorStateFunction(it) }
            .subscribe().also { compositeDisposable.add(it) }

    protected fun transformState(reduceStateFunction: (S) -> S) {
        stateTransformer.transformState(reduceStateFunction)
    }

    private class StateTransformer<S : State>(private val stateHolder: Subject<S>) {
        @Synchronized
        fun transformState(reduceStateFunction: (S) -> S) {
            val oldState = stateHolder.firstOrError().blockingGet()
            stateHolder.onNext(reduceStateFunction(oldState))
        }
    }
}
