package revolut.com.mywallet.domain

import io.reactivex.Observable
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.ConversionTable
import revolut.com.mywallet.entities.Currency
import java.util.concurrent.TimeUnit

class GetConversionsUseCase(private val repository: IConversionsRepository): IGetConversionsUseCase {

    override fun getConversionTable(base: Currency): Observable<ConversionTable> {
        return Observable
            .interval(1, TimeUnit.SECONDS)
            .flatMap { repository.getConversionTable(base) }
    }

    override fun reorderConversions(base: Conversion, conversions: List<Conversion>): Observable<List<Conversion>> {
        return repository.reorderConversions(base, conversions)
    }
}
