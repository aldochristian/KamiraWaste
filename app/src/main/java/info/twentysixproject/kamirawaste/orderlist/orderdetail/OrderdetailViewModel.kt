package info.twentysixproject.kamirawaste.orderlist.orderdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.functions.FirebaseFunctions
import info.twentysixproject.kamirawaste.orderlist.Orders

class OrderdetailViewModel : ViewModel() {
    //DB Instance
    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //Authentication Instance
    val user: FirebaseUser? = auth.currentUser
    var orderDetail= MutableLiveData<Orders>()

    var idOrder:String = ""

    var locationMaps:GeoPoint? = null
    //Load the detail
    fun fetchFirestore(idOrder:String){

        this.idOrder = idOrder

        db.collection("orders")
            .document(idOrder)
            .get()
            .addOnSuccessListener {
                if(it != null){
                    //show Order id, location address, Nama pengirim , no telpon, notes, time pick up, status
                    orderDetail.value = Orders(
                        idOrder,
                        it.get("location") as GeoPoint,
                            it.get("address") as String,
                            it.get("senderName") as String,
                            it.get("senderContact") as String,
                            it.get("senderNote") as String,
                            it.get("dateCreated").toString(),
                            it.get("status").toString(),
                        "", "", "")

                }
            }.addOnFailureListener{}

        locationMaps = orderDetail.value?.geoCoor
    }

    private val _actionNumber= MutableLiveData<Int>()
    val actionNumber: LiveData<Int>
        get() = _actionNumber
    val COMPLETE_TASK:Int = 4
    val SET_CANCEL:Int = 2

    fun standbyEvent(){ _actionNumber.value = 0 }

    fun onClickForCancel(){ _actionNumber.value = SET_CANCEL}

    fun cancelRequest(){
        val orderRef = db.collection("orders").document(idOrder)
        val userRef = db.collection("users").document(user!!.uid).collection("orders").document(idOrder)

        db.runBatch { batch ->
            batch.update(orderRef, "status", "cancel")
            batch.update(orderRef, "update", FieldValue.serverTimestamp())
            batch.update(userRef, "status", "cancel")
        }.addOnCompleteListener {
           _actionNumber.value = COMPLETE_TASK //Completed to cancel
        }
    }

    fun completeTask(){
        _actionNumber.value = COMPLETE_TASK
    }

}