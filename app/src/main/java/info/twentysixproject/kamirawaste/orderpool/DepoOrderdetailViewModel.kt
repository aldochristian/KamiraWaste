package info.twentysixproject.kamirawaste.orderpool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.functions.FirebaseFunctions

class DepoOrderdetailViewModel: ViewModel() {

    //DB Instance
    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //Authentication Instance
    val user: FirebaseUser? = auth.currentUser
    var orderDetail= MutableLiveData<Orders>()

    var locationMaps:GeoPoint? = null

    private val _actionNumber= MutableLiveData<Int>()
    val actionNumber: LiveData<Int>
        get() = _actionNumber
    val COMPLETE_TASK:Int = 4
    val SET_SCHEDULED:Int = 1
    val SET_COMPLETE:Int = 3

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

    fun onClickForScheduled(){ _actionNumber.value = SET_SCHEDULED }

    fun onClickForCompleted(){ _actionNumber.value = SET_COMPLETE}

    var functions: FirebaseFunctions = FirebaseFunctions.getInstance()
    var idOrder:String = ""

    fun standbyEvent(){ _actionNumber.value = 0 }

    fun completedRequest(weight: Int): Task<String> {
        val data = hashMapOf(
            "idOrder" to idOrder,
            "weight" to weight
        )

        return functions
            .getHttpsCallable("changeStatusToCompleted")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }

    fun scheduledRequest(): Task<String> {

        val data = hashMapOf(
            "idorder" to this.idOrder
        )

        return functions
            .getHttpsCallable("changeStatusToScheduled")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }

    fun completeTask(){
        _actionNumber.value = COMPLETE_TASK
    }
}