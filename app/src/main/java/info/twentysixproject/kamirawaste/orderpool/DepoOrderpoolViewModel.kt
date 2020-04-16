package info.twentysixproject.kamirawaste.orderpool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import info.twentysixproject.kamirawaste.Utils.Utils

class DepoOrderpoolViewModel : ViewModel() {
    //DB Instance
    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //Authentication Instance
    val user: FirebaseUser? = auth.currentUser

    val orderList= MutableLiveData<List<Orders>>()

    private val _navigateToOrderDetail = MutableLiveData<Orders>()
    val navigateToOrderDetail
        get() = _navigateToOrderDetail

    fun fetchFirestore(){
        val orderFetch : MutableList<Orders> = mutableListOf()
        // Get entire document listed for Order
        // Order which have same with user ID
        // Order by latest date
        db.collection("orders")
            .whereEqualTo("status", "pending")
            .orderBy("dateCreated", Query.Direction.ASCENDING)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    orderFetch.add(Orders(
                        document.id,
                        document.get("location") as GeoPoint,
                        document.get("address").toString(),
                        document.get("senderName").toString(),
                        document.get("senderContact").toString(),
                        document.get("senderNote").toString(),
                        Utils.convertTimeFromFirebase(document.get("dateCreated") as Timestamp),
                        "",
                        "",
                        "",
                        ""))
                }
                orderList.value = orderFetch
            }
    }

    fun getOrderList(): LiveData<List<Orders>> {
        return orderList
    }

    fun onDetailClicked(data: Orders) {
        _navigateToOrderDetail.value = data
    }

    fun onOrderDetailNavigated() {
        _navigateToOrderDetail.value = null
    }
}
