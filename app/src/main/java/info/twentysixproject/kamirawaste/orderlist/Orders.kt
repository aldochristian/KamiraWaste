package info.twentysixproject.kamirawaste.orderlist

import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName

/*
data class Orders (
    val id: String,
    @SerializedName("status") val status: String,
    @SerializedName("dateCreated") val dateCreated: String?,
    @SerializedName("loc") val loc: GeoPoint?,
    @SerializedName("address") val address: String?,
    @SerializedName("collectorName") val collectorName: String?,
    @SerializedName("wasteType")val wasteType: String?,
    @SerializedName("weight")val weight: String?,
    val dateClosed: String?

)
 */
data class Orders(
    val id: String,
    @SerializedName("geoCoor") val geoCoor: GeoPoint?,
    @SerializedName("address") val address: String?,
    @SerializedName("senderName") val senderName: String?,
    @SerializedName("senderContact") val senderContact: String?,
    @SerializedName("senderNote") val senderNote: String?,
    @SerializedName("dateCreated") val dateCreated: String?,
    val status: String,
    val update: String?,
    val userDepo: String,
    val weight: String
)