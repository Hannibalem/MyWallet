package revolut.com.mywallet.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("latest")
    fun getConversionTable(@Query("base") base: String): Single<ConversionTableData>
}
