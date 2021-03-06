package info.twentysixproject.kamirawaste.orderlist.orderdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import info.twentysixproject.kamirawaste.R
import info.twentysixproject.kamirawaste.databinding.FragmentDetailOrderBinding
import info.twentysixproject.kamirawaste.databinding.FragmentOrderdetailBinding
import info.twentysixproject.kamirawaste.orderlist.OrderlistViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderdetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderdetailFragment : Fragment() {

    private lateinit var viewModel : OrderdetailViewModel
    private var orderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDetailOrderBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail_order, container, false)

        viewModel = ViewModelProvider(this).get(OrderdetailViewModel::class.java)
        binding.orderdetailViewModel = viewModel

        viewModel.fetchFirestore(arguments!!.getString("idOrder"))
        /*viewModel.getOrderDetail().observe(viewLifecycleOwner, Observer {
            Log.i("Orderdetail", "Here "+it)
        })*/

        //Observe action to be taken (change status)
        viewModel.actionNumber.observe(viewLifecycleOwner, Observer {
            when(it){
                2 -> {
                    viewModel.cancelRequest()
                    viewModel.completeTask()
                }
                4 -> {
                    showDialogComplete()
                }
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Receive value of detail key ID
        orderId = arguments!!.getString("idOrder")

        view.findViewById<Button>(R.id.frdetail_btn)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_orderdetailFragment_to_orderactionFragment, null)
        )
    }

    fun showDialogComplete(){
        viewModel.standbyEvent()
        Log.d(this.toString(), "Complete task to scheduled")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderdetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderdetailFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
