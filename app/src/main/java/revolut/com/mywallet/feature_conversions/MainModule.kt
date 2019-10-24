package revolut.com.mywallet.feature_conversions

import dagger.Module
import dagger.Provides
import revolut.com.mywallet.domain.IGetConversionsUseCase
import revolut.com.mywallet.utils.scopes.PerActivity

@Module
abstract class MainModule {

    @Module
    companion object {

        @PerActivity
        @JvmStatic
        @Provides
        fun provideStore(useCase: IGetConversionsUseCase): IMainStore = MainStore(useCase)

        @PerActivity
        @JvmStatic
        @Provides
        fun provideViewModel(store: IMainStore) = MainViewModel(store)
    }
}
