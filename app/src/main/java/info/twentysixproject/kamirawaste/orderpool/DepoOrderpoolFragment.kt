package info.twentysixproject.kamirawaste.orderpool

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import info.twentysixproject.kamirawaste.R
import info.twentysixproject.kamirawaste.databinding.FragmentDepoOrderpoolBinding
import info.twentysixproject.kamirawaste.orderpool.adapter.DepoOrderpoolAdapter
import info.twentysixproject.kamirawaste.orderpool.adapter.DepoOrderpoolListener

class DepoOrderpoolFragment : Fragment() {

    companion object {
        fun newInstance() = DepoOrderpoolFragment()
    }

    private lateinit var viewModel: DepoOrderpoolViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDepoOrderpoolBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_depo_orderpool, container,
            false
        )

        viewModel = ViewModelProvider(this).get(DepoOrderpoolViewModel::class.java)

        binding.depoOrderPoolViewModel = viewModel

        val adapter = DepoOrderpoolAdapter(DepoOrderpoolListener {
            viewModel.onDetailClicked(it)
        })
        binding.rvDepoOrderpool.adapter = adapter

        viewModel.fetchFirestore()
        viewModel.getOrderList().observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        // Add an Observer on the state variable for Navigating when and item is clicked.
        //OrderlistFragmentDirections.actionOrderlistFragmentToOrderdetailFragment(it.id))
        viewModel.navigateToOrderDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    DepoOrderpoolFragmentDirections.actionDepoOrderpoolFragmentToDepoOrderdetail(it.id))
                viewModel.onOrderDetailNavigated()
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

}
