package info.twentysixproject.kamirawaste

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    internal var db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val user: String? = FirebaseAuth.getInstance().uid

    private lateinit var functions: FirebaseFunctions
    var firstTimeLogin: Boolean = false // Ensure data admin initiate for first time login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FCM initiate
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.notification_channel_id)
            val channelName = getString(R.string.promotion_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }

        //val userID: String = intent.getStringExtra("uid")
        functions = FirebaseFunctions.getInstance()
        currentToken(user) // FCM Token stored and check

        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.promotion_notification_channel_name))
            .addOnCompleteListener { _ ->
                //var msg = getString(R.string.msg_subscribed)
                //if (!task.isSuccessful) {
                //msg = getString(R.string.msg_subscribe_failed)
                //}
                //Log.d(TAG, msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }

        // Navigation controller
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController

        setupBottomNavMenu(navController) //Bottom Module
        navController.addOnDestinationChangedListener { _, destination, _ ->
            try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
        }
        //End of navigation controller
    }

    //======Navigation bottom menu=========//
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view_bottom)
        bottomNav?.setupWithNavController(navController)

    } // End of navigation bottom menu

    //======FCM Token======//
    private fun currentToken(userUid: String?){

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                // Stored token
                val checkDoc = db.collection("users").document(userUid!!)

                if(firstTimeLogin){
                    val data = hashMapOf(
                        "fcmToken" to token,
                        "point" to 0.00,
                        "contribution" to 0.00)
                    db.collection("users").document(userUid)
                        .set(data, SetOptions.merge())
                        .addOnSuccessListener {
                            //adminConfiguration("test")
                        }
                        .addOnFailureListener {
                        }
                }else {
                    checkDoc.update("lastLogin", FieldValue.serverTimestamp())
                        .addOnSuccessListener {
                        }.addOnFailureListener {
                            //No docs found to update
                        }
                }

            })
    }
}
