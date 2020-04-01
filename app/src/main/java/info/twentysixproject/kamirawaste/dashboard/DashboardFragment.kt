package info.twentysixproject.kamirawaste.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel : DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

        view.findViewById<FloatingActionButton>(R.id.frdashboard_btnpick)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_dashboardtopickup, null)
        )

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
