package info.twentysixproject.kamirawaste.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
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

    fun fetchPointsProfile(){

        db.collection("users")
            .document(user!!.uid)
            .get()
            .addOnSuccessListener {
                if(it != null){
                    balanceCash.value = it.get("balanceCash") as Long
                    balancePoint.value = it.get("balancePoint") as Long
                    fullName.value = it.get("fullname").toString()
                }
            }.addOnFailureListener{}

        //balance.value = pointFetch
    }

    fun getPoints(): LiveData<String> {
        return myPoints
    }

}