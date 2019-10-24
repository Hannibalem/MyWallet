package revolut.com.mywallet

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.then
import com.nhaarman.mockito_kotlin.times
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import revolut.com.mywallet.domain.IGetConversionsUseCase
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.ConversionTable
import revolut.com.mywallet.entities.Currency
import revolut.com.mywallet.feature_conversions.MainState
import revolut.com.mywallet.feature_conversions.MainStore
import revolut.com.mywallet.utils.store.State

@RunWith(MockitoJUnitRunner::class)
class MainStoreUnitTest {

    @Mock
    private lateinit var useCaseMock: IGetConversionsUseCase
    @Mock
    private lateinit var consumerMock: Consumer<MainState>
    @InjectMocks
    private lateinit var tested: MainStore

    companion object {

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        }
    }

    @Test
    fun `Retrieves conversions`() {
        val conversion = Conversion(Currency.USD, 2.0)
        val otherConversion = Conversion(Currency.EUR, 2.0)
        val table = ConversionTable(Currency.USD, listOf(otherConversion))

        given(useCaseMock.reorderConversions(conversion, emptyList())).willReturn(Observable.just(listOf(conversion)))
        given(useCaseMock.getConversionTable(Currency.USD)).willReturn(Observable.just(table))

        val subscribe = tested.subscribe(consumerMock)
        tested.getConversions(conversion)

        val captor = argumentCaptor<MainState>()
        then(consumerMock).should(times(3)).accept(captor.capture())
        assertThat(captor.allValues[1].status).isEqualTo(State.Status.LOADING)
        assertThat(captor.allValues[1].model.base).isEqualTo(conversion)
        assertThat(captor.allValues[1].model.conversions.size).isEqualTo(1)
        assertThat(captor.allValues[1].model.conversions[0]).isEqualTo(conversion)
        assertThat(captor.allValues[2].status).isEqualTo(State.Status.READY)
        assertThat(captor.allValues[1].model.base).isEqualTo(conversion)
        assertThat(captor.allValues[2].model.conversions.size).isEqualTo(2)
        assertThat(captor.allValues[2].model.conversions[0]).isEqualTo(conversion)
        assertThat(captor.allValues[2].model.conversions[1].currency).isEqualTo(Currency.EUR)
        assertThat(captor.allValues[2].model.conversions[1].value).isEqualTo(conversion.value * otherConversion.value)

        subscribe.dispose()
    }
}
