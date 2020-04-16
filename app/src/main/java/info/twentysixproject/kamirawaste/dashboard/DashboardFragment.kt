package info.twentysixproject.kamirawaste.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.functions.FirebaseFunctions
import info.twentysixproject.kamirawaste.R
import info.twentysixproject.kamirawaste.databinding.FragmentDashboardBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel : DashboardViewModel
    lateinit var functions: FirebaseFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        functions = FirebaseFunctions.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard, container, false)

        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        binding.dashboardViewModel = viewModel

        viewModel.fetchPointsProfile()

        binding.lifecycleOwner = this

        viewModel.firstLogin.observe(viewLifecycleOwner, Observer {
            Log.d("test", it.toString())
            if (it){
                firstTimeSetup("Test")
                //viewModel.createdUser()
            }
        })

        viewModel.endUserIdentification.observe(viewLifecycleOwner, Observer {
            if(it){ assignAccess = ENDUSER }
            else{ assignAccess = DEPOUSER }
        })

        // : Step 1.7 call create channel
        createChannel(
            getString(R.string.egg_notification_channel_id),
            getString(R.string.egg_notification_channel_name)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navigation bottom
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view_bottom)
        navBar?.visibility = View.VISIBLE //Navigation hidden

        view.findViewById<FloatingActionButton>(R.id.frdashboard_btnpick).setOnClickListener {
            when(assignAccess){
                ENDUSER -> {
                    findNavController().navigate(R.id.action_dashboardtopickup, null)
                }
                DEPOUSER -> {
                    findNavController().navigate(R.id.action_dashboardtoorderpool, null)
                }
            }
        }

        view.findViewById<FloatingActionButton>(R.id.frdashboard_btndepo)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_dashboardFragment_to_depotlistFragment, null)
        )

    }

    private fun createChannel(channelId: String, channelName: String) {
        // : Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // : Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// : Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.breakfast_notification_channel_description)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
        // : Step 1.6 END create a channel
    }

    //======First sign configuration=====//
    private fun firstTimeSetup(text: String): Task<String> {
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "text" to text,
            "push" to true
        )

        return functions
            .getHttpsCallable("createUser")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }

    val ENDUSER:Int = 1
    val DEPOUSER:Int = 2
    var assignAccess: Int = 0

    companion object {

    }
}
