package revolut.com.mywallet

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import revolut.com.mywallet.data.ApiService
import revolut.com.mywallet.data.ConversionRepository
import revolut.com.mywallet.data.ConversionTableData
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.Currency

@RunWith(MockitoJUnitRunner::class)
class ConversionRepositoryUnitTest {

    @Mock
    private lateinit var serviceMock: ApiService
    @InjectMocks
    private lateinit var tested: ConversionRepository

    companion object {

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        }
    }

    @Test
    fun `Retrieves conversions table`() {
        val currency = Currency.USD
        val table = ConversionTableData("USD", mapOf("EUR" to 1.0))

        given(serviceMock.getConversionTable(currency.name)).willReturn(Single.just(table))

        val observer = tested.getConversionTable(currency).test()

        observer.assertComplete()
            .assertNoErrors()
            .assertValue {
                it.base == Currency.USD
                        && it.conversions.size == 1
                        && it.conversions[0].currency == Currency.EUR
                        && it.conversions[0].value == 1.0
            }
        observer.dispose()
    }

    @Test
    fun `Reorders conversions`() {
        val conversion = Conversion(Currency.USD, 1.0)
        val conversions = listOf(Conversion(Currency.EUR, 1.0), Conversion(Currency.USD, 2.0))

        val observer = tested.reorderConversions(conversion, conversions).test()

        observer.assertComplete()
            .assertNoErrors()
            .assertValue {
                it.size == 2
                        && it[0].currency == Currency.USD && it[0].value == 1.0
                        && it[1].currency == Currency.EUR && it[1].value == 1.0
            }
        observer.dispose()
    }
}
