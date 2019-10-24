package revolut.com.mywallet.utils.store

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface Store <S : State> {

    fun subscribe(consumer: Consumer<S>): Disposable

    fun stop()

    fun dispose()
}
