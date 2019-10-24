package revolut.com.mywallet

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.then
import io.reactivex.functions.Consumer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.Currency
import revolut.com.mywallet.feature_conversions.*
import revolut.com.mywallet.utils.store.State

@RunWith(MockitoJUnitRunner::class)
class MainViewModelUnitTest {

    @Mock
    private lateinit var storeMock: IMainStore
    @InjectMocks
    private lateinit var tested: MainViewModel

    @Test
    fun `Conversions created when state is ready`() {
        val conversion = Conversion(Currency.USD, 2.0)
        tested.selectedConversion = conversion
        val state = MainState(
            State.Status.READY,
            ConversionModel(conversion, listOf(conversion, Conversion(Currency.EUR, 3.0)))
        )

        tested.onStart()

        argumentCaptor<Consumer<MainState>>().apply {
            then(storeMock).should().subscribe(capture())

            firstValue.accept(state)

            assertThat(tested.loading.get()).isEqualTo(false)
            assertThat(tested.conversions.get()!!.size).isEqualTo(2)
            assertThat(tested.conversions.get()!![0].hasFocus.get()).isEqualTo(true)
            assertThat(tested.conversions.get()!![0].name).isEqualTo(Currency.USD.name)
            assertThat(tested.conversions.get()!![1].hasFocus.get()).isEqualTo(false)
            assertThat(tested.conversions.get()!![1].name).isEqualTo(Currency.EUR.name)
        }
    }
}
