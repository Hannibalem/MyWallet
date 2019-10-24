package revolut.com.mywallet

import dagger.Module
import dagger.android.ContributesAndroidInjector
import revolut.com.mywallet.feature_conversions.MainActivity
import revolut.com.mywallet.feature_conversions.MainModule
import revolut.com.mywallet.utils.scopes.PerActivity

@Module
internal abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun contributeMainActivityInjector(): MainActivity
}
