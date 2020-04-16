package info.twentysixproject.kamirawaste.orderlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import info.twentysixproject.kamirawaste.R
import info.twentysixproject.kamirawaste.databinding.FragmentOrderlistBinding
import info.twentysixproject.kamirawaste.orderlist.adapter.OrderAdapter
import info.twentysixproject.kamirawaste.orderlist.adapter.OrderListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//private val model: OrderlistViewModel by activityViewModels()


class OrderlistFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel : OrderlistViewModel

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
        val binding: FragmentOrderlistBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_orderlist, container,
            false
        )

        viewModel = ViewModelProvider(this).get(OrderlistViewModel::class.java)
        binding.orderlistViewModel = viewModel

        val adapter = OrderAdapter(OrderListener {
            viewModel.onDetailClicked(it)
        })
        binding.rvOrderlist.adapter = adapter

        viewModel.fetchFirestore()
        viewModel.getOrderList().observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        // Add an Observer on the state variable for Navigating when and item is clicked.
        viewModel.navigateToOrderDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    OrderlistFragmentDirections.actionOrderlistFragmentToOrderdetailFragment(it.id))
                viewModel.onOrderDetailNavigated()
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    companion object {

    }
}
