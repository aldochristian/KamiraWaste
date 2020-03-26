package info.twentysixproject.kamirawaste.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import info.twentysixproject.kamirawaste.R

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Button edit profile
        view.findViewById<FloatingActionButton>(R.id.fab_editprofile)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_editprofileFragment, null)
        )
    }
}
