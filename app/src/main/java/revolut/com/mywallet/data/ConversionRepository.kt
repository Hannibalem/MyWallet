package revolut.com.mywallet.data

import io.reactivex.Observable
import revolut.com.mywallet.domain.IConversionsRepository
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.ConversionTable
import revolut.com.mywallet.entities.Currency

class ConversionRepository(private val service: ApiService): IConversionsRepository {

    override fun getConversionTable(base: Currency): Observable<ConversionTable> {
        return service.getConversionTable(base.name)
            .map { ConversionTable(base, it.rates.map { entry -> transform(entry) }.filterNot { it.currency == Currency.NONE }) }
            .toObservable()
    }

    private fun transform(data: Map.Entry<String, Double>): Conversion {
        return try {
            Conversion(Currency.valueOf(data.key), data.value)
        } catch(e: IllegalArgumentException) {
            Conversion(Currency.NONE, data.value)
        }
    }

    override fun reorderConversions(base: Conversion, conversions: List<Conversion>): Observable<List<Conversion>> {
        return Observable.fromCallable {
            conversions.toMutableList().apply {
                try {
                    val selectedCurrency = first { it.currency == base.currency }
                    remove(selectedCurrency)
                    add(0, base)
                } catch (e: NoSuchElementException) {
                    add(base)
                }
            }
        }
    }
}
