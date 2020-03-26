package info.twentysixproject.kamirawaste.depotlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import info.twentysixproject.kamirawaste.R

class DepotlistFragment : Fragment() {

    companion object {
        fun newInstance() = DepotlistFragment()
    }

    private lateinit var viewModel: DepotlistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_depotlist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DepotlistViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
