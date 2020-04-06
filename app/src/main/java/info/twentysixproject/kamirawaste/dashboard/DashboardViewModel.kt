package info.twentysixproject.kamirawaste.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import info.twentysixproject.kamirawaste.profile.Profile

class DashboardViewModel : ViewModel(){
    //DB Instance
    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //Authentication Instance
    val user: FirebaseUser? = auth.currentUser

    val balanceCash = MutableLiveData<Long>(0)
    val balancePoint = MutableLiveData<Long>(0)
    val fullName = MutableLiveData<String>()

    private val _mypoints = MutableLiveData<String>()
    val myPoints: LiveData<String>
        get() = _mypoints

    init {
        // Firebase cache
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        queryFirstLogin()
    }

    fun fetchPointsProfile(){

        db.collection("users")
            .document(user!!.uid)
            .get()
            .addOnSuccessListener {
                if(it != null){
                    try {
                        balanceCash.value = it.get("balanceCash") as Long
                        balancePoint.value = it.get("balancePoint") as Long
                        fullName.value = it.get("fullname").toString()
                    }catch (e: TypeCastException){
                        balanceCash.value = 0
                        balancePoint.value = 0
                    }

                }
            }.addOnFailureListener{}

        //balance.value = pointFetch
    }

    fun getPoints(): LiveData<String> {
        return myPoints
    }

    private val _firstLogin = MutableLiveData<Boolean>()
    val firstLogin: LiveData<Boolean>
        get() = _firstLogin

    fun queryFirstLogin(){
        var value = false
        val docRef = db.collection("users").document(user!!.uid)
        docRef.get()
            .addOnSuccessListener{
                if(it.data.isNullOrEmpty()){
                    _firstLogin.value = true
                }
            }
    }

    fun inquiryUserDocs(): LiveData<Boolean>{
        return firstLogin
    }

    fun createdUser(){
        _firstLogin.value = false
    }

}