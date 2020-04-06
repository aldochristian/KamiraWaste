package info.twentysixproject.kamirawaste.orderlist.orderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import info.twentysixproject.kamirawaste.orderlist.Orders

class OrderdetailViewModel : ViewModel() {
    //DB Instance
    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //Authentication Instance
    val user: FirebaseUser? = auth.currentUser
    var orderDetail= MutableLiveData<Orders>()

    init {
        //fetchFirestore()
    }

    //Load the detail
    fun fetchFirestore(idOrder:String){
        val orderFetch : MutableLiveData<Orders>

        db.collection("orders")
            .document(idOrder)
            .get()
            .addOnSuccessListener {
                if(it != null){
                    //show Order id, location address, Nama pengirim , no telpon, notes, time pick up, status
                    orderDetail.value = Orders(
                        idOrder,
                        null,
                            it.get("address") as String,
                            it.get("senderName") as String,
                            it.get("senderContact") as String,
                            it.get("senderNote") as String,
                            it.get("dateCreated").toString(),
                        "",
                        "",
                        "")

                }
            }.addOnFailureListener{}
    }

}