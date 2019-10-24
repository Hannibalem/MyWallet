package revolut.com.mywallet

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import revolut.com.mywallet.domain.GetConversionsUseCase
import revolut.com.mywallet.domain.IConversionsRepository
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.ConversionTable
import revolut.com.mywallet.entities.Currency
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class GetConversionsUseCaseUnitTest {

    @Mock
    private lateinit var repositoryMock: IConversionsRepository
    @InjectMocks
    private lateinit var tested: GetConversionsUseCase

    companion object {

        val testScheduler = TestScheduler()

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
            RxJavaPlugins.setIoSchedulerHandler { testScheduler }
            RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        }
    }

    @Test
    fun `Retrieves conversions table`() {

        val conversionTable = ConversionTable(Currency.NONE, emptyList())
        val currency = Currency.NONE

        given(repositoryMock.getConversionTable(currency)).willReturn(Observable.just(conversionTable))

        val observer = tested.getConversionTable(currency).test()

        observer.assertNotTerminated()
            .assertNoErrors()
            .assertValueCount(0)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        observer.assertValueCount(1)
            .assertValue { it == conversionTable }

        observer.dispose()
    }

    @Test
    fun `Retrieves reordered conversions`() {
        val conversion = Conversion(Currency.NONE, 0.0)
        val conversions = emptyList<Conversion>()
        val resultConversions = mock<List<Conversion>>()

        given(repositoryMock.reorderConversions(conversion, conversions)).willReturn(Observable.just(resultConversions))

        val observer = tested.reorderConversions(conversion, conversions).test()

        observer.assertComplete().assertNoErrors().assertValue { it == resultConversions }

        observer.dispose()
    }
}
