package revolut.com.mywallet

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App: DaggerApplication() {

    lateinit var applicationComponent: ApplicationComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build().also { applicationComponent = it }
    }
}
