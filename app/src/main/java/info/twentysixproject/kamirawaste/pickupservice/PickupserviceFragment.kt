package info.twentysixproject.kamirawaste.pickupservice

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import info.twentysixproject.kamirawaste.R
import info.twentysixproject.kamirawaste.databinding.FragmentPickupserviceBinding
import kotlinx.android.synthetic.main.fragment_pickupservice.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PickupserviceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PickupserviceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewModel: PickupserviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var myAddressPicked: String? = null
    private var latitude = 0.0
    private var longtitude = 0.0
    private var myLocationPicked = LatLng(0.0,0.0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPickupserviceBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pickupservice, container, false)

        binding.frpickTxtaddress.setText(myAddressPicked)

        viewModel = ViewModelProviders.of(this).get(PickupserviceViewModel::class.java)
        binding.model = viewModel

        binding.lifecycleOwner = this
        return binding.root
    }

    /**
     * Interface module listener
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Button frpick_btnschedule listener
        view.findViewById<Button>(R.id.frpick_btnschedule)?.setOnClickListener{
            showSingleChoiceDialog()
        }
        //Button pick location
        view.findViewById<ImageButton>(R.id.frpick_location)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_pickupserviceFragment_to_locationpickDialogFragment, null)
        )

        //Monitoring when client submit order
        viewModel.hasComplete.observe(viewLifecycleOwner, Observer {
            it.let {
                if(it){ Navigation.findNavController(view).navigate(R.id.action_pickupserviceFragment_to_submitrequestDialogFragment) } // Call Dialog when success
            }
        })

        //Receive value after pick location
        myAddressPicked = arguments!!.getString("locAddress")
        latitude = arguments!!.getDouble("coordLat", 0.00)
        longtitude = arguments!!.getDouble("coordLog", 0.00)
        myLocationPicked = LatLng(latitude!!, longtitude!!)

    }

    val PICKUP_TIME = arrayOf(
        "Pagi antara jam 9 sampai 12 siang", "Sore antara jam 3 sampai 6 sore"
    )
    var single_choice_selected: String? = null
    fun showSingleChoiceDialog() {
        single_choice_selected = PICKUP_TIME.get(0)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Pilih jadwal pengambilan")
        builder.setSingleChoiceItems(PICKUP_TIME, 0,
            DialogInterface.OnClickListener { dialogInterface, i ->
                single_choice_selected = PICKUP_TIME.get(i)
                viewModel.selectedSchedule(i)
            })
        builder.setPositiveButton(
            android.R.string.ok,
            DialogInterface.OnClickListener { dialogInterface, i ->
                frpick_txtschedule.setText("Pengambilan $single_choice_selected")
            })
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PickupserviceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PickupserviceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
