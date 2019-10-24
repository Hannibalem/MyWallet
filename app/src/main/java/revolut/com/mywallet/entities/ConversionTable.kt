package revolut.com.mywallet.entities

data class ConversionTable(
    val base: Currency,
    val conversions: List<Conversion>
)
