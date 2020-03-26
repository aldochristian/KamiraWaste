package info.twentysixproject.kamirawaste.pickupservice

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private var myLocationPicked = GeoPoint(0.0,0.0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPickupserviceBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pickupservice, container, false)

        binding.frpickTxtaddress.setText(myAddressPicked)

        viewModel = ViewModelProvider(this).get(PickupserviceViewModel::class.java)
        binding.model = viewModel


        //invalid please check again
        //trigger to store order
        viewModel.createOrder.observe(viewLifecycleOwner, Observer {
            if(it){
                //Triggered -> run validation -> pass then set null
                val isValid = validateOrder(binding)
                if(isValid){
                    //trigger to save change
                    viewModel.storeFirestore()
                }else{
                    // trigger dialog data invalid
                    dialogInvalidOrder()
                }
                viewModel.invalid() //Set to null condition
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    fun validateOrder(binding: FragmentPickupserviceBinding):Boolean {
        if(binding.frpickTxtsender.text.isNullOrBlank()){
            return false
        }else if(binding.frpickTxtphone.text.isNullOrBlank()){
            return false
        }else if(binding.frpickTxtaddress.text.isNullOrBlank()){
            return false
        }else if(binding.frpickTxtaddress.text.isNullOrBlank()){
            return false
        }
        return true
    }

    fun dialogInvalidOrder(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_warning)

        val lp : WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.findViewById<TextView>(R.id.title).text = "Yaay ..."
        dialog.findViewById<TextView>(R.id.content).text = "Order anda gagal dibuat, periksa kembali informasi yang anda berikan"

        dialog.show()
        dialog.window?.attributes = lp

        dialog.findViewById<Button>(R.id.bt_close).setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navigation bottom
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view_bottom)
        navBar?.visibility = View.INVISIBLE //Navigation hidden

        //Button frpick_btnschedule listener
        view.findViewById<Button>(R.id.frpick_btnschedule)?.setOnClickListener{
            showSingleChoiceDialog()
        }
        //Button pick location
        view.findViewById<ImageButton>(R.id.frpick_location)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_pickupserviceFragment_to_locationpickDialogFragment, null)
        )

        //Monitoring when client submit order
        /*viewModel.hasComplete.observe(viewLifecycleOwner, Observer {
            it.let {
                if(it){ Navigation.findNavController(view).navigate(R.id.action_pickupserviceFragment_to_submitrequestDialogFragment) } // Call Dialog when success
            }
        })*/

        //Receive value after pick location
        myAddressPicked = arguments!!.getString("locAddress")
        viewModel.address.value = myAddressPicked
        latitude = arguments!!.getDouble("coordLat", 0.00)
        longtitude = arguments!!.getDouble("coordLog", 0.00)
        myLocationPicked = GeoPoint(latitude!!, longtitude!!)
        viewModel.location.value = myLocationPicked

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

}
