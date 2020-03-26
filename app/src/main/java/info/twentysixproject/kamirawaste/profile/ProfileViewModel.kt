package info.twentysixproject.kamirawaste.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel: ViewModel() {

    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val user: FirebaseUser? = auth.currentUser

    //button for save change
    private val _saveProfileChange = MutableLiveData<Boolean>()
    val saveProfileChange
        get() = _saveProfileChange

    val id = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val address = MutableLiveData<String>()

    //fetch profile
    fun fetchProfile(){
        _saveProfileChange.value = false

        db.collection("users")
            .document(user!!.uid)
            .get()
            .addOnSuccessListener {
                if(it != null){
                    Log.i("ProfileViewModel", "Succes load "+it)
                    id.value = user.uid
                    name.value = it.get("fullname").toString()
                    address.value = it.get("address").toString()
                    city.value = it.get("city").toString()
                    phone.value = it.get("phone").toString()
                }
            }.addOnFailureListener{
                Log.i("ProfileViewModel", "Fail to load "+it)
            }
    }

    //save trigger
    fun saveOnClick(){
        saveProfileChange.value = true
    }

    //save changes
    fun saveChange(){
        Log.i("ProfileViewModel", "Success to change")
    }
}