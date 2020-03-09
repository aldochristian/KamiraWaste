package info.twentysixproject.kamirawaste.pickupservice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import info.twentysixproject.kamirawaste.R

/**
 * A simple [Fragment] subclass.
 */
class SubmitrequestDialogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_submitrequest_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_confirm)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_submitrequestDialogFragment_to_dashboardFragment, null)
        )
    }

}
