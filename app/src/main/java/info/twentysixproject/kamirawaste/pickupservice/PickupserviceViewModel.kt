package info.twentysixproject.kamirawaste.pickupservice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class PickupserviceViewModel : ViewModel() {
    //DB Instance
    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //Authentication Instance
    val user: FirebaseUser? = auth.currentUser

    private val _createOrder = MutableLiveData<Boolean>()
    val createOrder: LiveData<Boolean>
        get() = _createOrder

    private val _hasLoaded = MutableLiveData<Boolean>()
    val hasLoaded: LiveData<Boolean>
        get() = _hasLoaded

    /**
     * Activated status is have three status. Each status has limited access
     * registered, activated, verified
     */
    private val _activatedAccount = MutableLiveData<String>()
    val activatedAccount: LiveData<String>
        get() = _activatedAccount

    val address = MutableLiveData<String>()
    val location = MutableLiveData<GeoPoint>()

    val senderName = MutableLiveData<String>()
    val senderContact = MutableLiveData<String>()
    val senderNote = MutableLiveData<String>()
    //Choice of pickup
    var selectedChoice = MutableLiveData<Int>()

    fun selectedSchedule(arrayInt: Int){
        selectedChoice.value = arrayInt
    }
    //Choice of extra

    init {
        _createOrder.value = false
    }

    fun selectedExtra(){}

    fun storeFirestore(){

        val orderId: DocumentReference = db.collection("orders").document()
        val orderRef = db.collection("orders").document(orderId.id)
        val userRef = db.collection("users").document(user!!.uid).collection("orders").document(orderId.id)

        val orderData = hashMapOf(
            "location" to location.value,
            "address" to location.value,
            "senderName" to senderName.value,
            "senderContact" to senderContact.value,
            "senderNote" to senderNote.value,
            "status" to "assigned",
            "dateCreated" to FieldValue.serverTimestamp())

        val orderDataForUser = hashMapOf(
            "dateCreated" to FieldValue.serverTimestamp()
        )

        db.runBatch { batch ->
            batch.set(orderRef, orderData)
            batch.set(userRef, orderDataForUser)
        }.addOnCompleteListener {
            //_hasComplete.value = true
        }.addOnFailureListener{
            //_hasComplete.value = false
        }

    }

    fun onClickSave(){
        _createOrder.value = true
        //storeFirestore()
    }

    fun  invalid(){
        _createOrder.value = false
    }

}