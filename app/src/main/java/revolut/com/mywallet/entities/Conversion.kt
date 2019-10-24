package revolut.com.mywallet.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Conversion(
    val currency: Currency,
    val value: Double
): Parcelable
