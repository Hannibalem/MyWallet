package revolut.com.mywallet.domain

import io.reactivex.Observable
import revolut.com.mywallet.entities.Conversion
import revolut.com.mywallet.entities.ConversionTable
import revolut.com.mywallet.entities.Currency

interface IConversionsRepository {

    fun getConversionTable(base: Currency): Observable<ConversionTable>

    fun reorderConversions(base: Conversion, conversions: List<Conversion>): Observable<List<Conversion>>
}
