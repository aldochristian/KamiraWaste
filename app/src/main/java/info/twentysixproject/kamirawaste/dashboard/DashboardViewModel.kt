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

    val balance = MutableLiveData<List<Point>>()
    val profile = MutableLiveData<List<Profile>>()

    private val _mypoints = MutableLiveData<String>()
    val myPoints: LiveData<String>
        get() = _mypoints

    fun fetchPoints(){
        val pointFetch : MutableList<Point> = mutableListOf()

        pointFetch.add(Point(
            "0001","200", "2000")
        )

        _mypoints.value = "200"
        balance.value = pointFetch
    }

    fun getPoints(): LiveData<String> {
        return myPoints
    }

    fun fetchProfile(){
        val profileFetch: MutableList<Profile> = mutableListOf()

        profileFetch.add(Profile(
            "", "Aldo", "NN", "Manado", "628114110823"
        ))

        profile.value = profileFetch
    }

}