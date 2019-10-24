package revolut.com.mywallet

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import revolut.com.mywallet.data.ApiService
import revolut.com.mywallet.data.ConversionRepository
import revolut.com.mywallet.domain.GetConversionsUseCase
import revolut.com.mywallet.domain.IConversionsRepository
import revolut.com.mywallet.domain.IGetConversionsUseCase
import javax.inject.Singleton

@Module
abstract class ApplicationModule {

    @Module
    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideContext(application: Application): Context {
            return application.applicationContext
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideRepository(service: ApiService): IConversionsRepository {
            return ConversionRepository(service)
        }

        @Provides
        @JvmStatic
        fun provideUseCase(repository: IConversionsRepository): IGetConversionsUseCase{
            return GetConversionsUseCase(repository)
        }
    }
}
