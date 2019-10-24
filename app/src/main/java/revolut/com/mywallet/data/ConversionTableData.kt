package revolut.com.mywallet.data

data class ConversionTableData(
    val base: String,
    val rates: Map<String, Double>
)
