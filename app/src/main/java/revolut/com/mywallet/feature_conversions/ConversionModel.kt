package revolut.com.mywallet.feature_conversions

import revolut.com.mywallet.entities.Conversion

data class ConversionModel(
    val base: Conversion,
    val conversions: List<Conversion>
)
