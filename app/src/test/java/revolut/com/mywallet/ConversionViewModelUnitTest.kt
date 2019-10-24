package revolut.com.mywallet

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.then
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.assertj.core.api.Assertions.assertThat
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.Currency
import revolut.com.mywallet.feature_conversions.ConversionViewModel
import revolut.com.mywallet.feature_conversions.IMainStore

@RunWith(MockitoJUnitRunner::class)
class ConversionViewModelUnitTest {

    @Mock
    private lateinit var storeMock: IMainStore

    private lateinit var tested: ConversionViewModel

    @Test
    fun `Properties are set`() {
        val conversion = Conversion(Currency.EUR, 2.0)
        tested = ConversionViewModel(storeMock, conversion, true)

        assertThat(tested.value.get()).isEqualTo("2")
        assertThat(tested.name).isEqualTo("EUR")
        assertThat(tested.hasFocus.get()).isEqualTo(true)
    }

    @Test
    fun `Focus set when focus listener is fired`() {
        tested = ConversionViewModel(storeMock, Conversion(Currency.EUR, 2.0), true)

        tested.onFocusListener(false)

        assertThat(tested.hasFocus.get()).isEqualTo(false)
    }

    @Test
    fun `New conversions retrieved when text listener is fired`() {
        tested = ConversionViewModel(storeMock, Conversion(Currency.EUR, 2.0), true)

        tested.onTextChangedListener("2.3")

        assertThat(tested.value.get()).isEqualTo("2.3")
        val captor = argumentCaptor<Conversion>()
        then(storeMock).should().getConversions(captor.capture())
        assertThat(captor.firstValue.value).isEqualTo(2.3)
        assertThat(captor.firstValue.currency).isEqualTo(Currency.EUR)
    }
}
