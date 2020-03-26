package info.twentysixproject.kamirawaste.depotlist

import com.google.firebase.firestore.GeoPoint

data class Depots (
    val id: String,
    val depoName: String,
    val depoPic: String?,
    val phone: String?,
    val geoCoor: GeoPoint?,
    val address: String?
)