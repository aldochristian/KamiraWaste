package info.twentysixproject.kamirawaste.profile

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import info.twentysixproject.kamirawaste.R
import info.twentysixproject.kamirawaste.databinding.FragmentEditprofileBinding


/**
 * A simple [Fragment] subclass.
 * Use the [EditprofileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditprofileFragment : Fragment() {

    private lateinit var viewModel : ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditprofileBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_editprofile, container, false)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.profileViewModel = viewModel

        //Load with current profile
        viewModel.fetchProfile()

        //Observer trigger of save change
        viewModel.saveProfileChange.observe(viewLifecycleOwner, Observer {
            if(it){
                //Triggered -> run validation -> pass then set null
                val isValid = validateChanges(binding)
                if(isValid){
                    //trigger to save change
                    viewModel.saveChange()
                }else{
                    // trigger dialog data invalid
                    dialogInvalidDataChange()
                }
                viewModel.saveProfileChange.value = false //Set to null condition
            }
        })


        binding.lifecycleOwner = this
        return binding.root
    }

    fun validateChanges(binding: FragmentEditprofileBinding):Boolean {
        if(binding.editprofileName.text.isNullOrBlank()){
            return false
        }else if(binding.editprofileAddress.text.isNullOrBlank()){
            return false
        }else if(binding.editprofileCity.text.isNullOrBlank()){
            return false
        }else if(binding.editprofilePhone.text.isNullOrBlank()){
            return false
        }

        return true
    }

    fun dialogInvalidDataChange(){

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_warning)

        val lp : WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.findViewById<TextView>(R.id.title).text = "Yaay ..."
        dialog.findViewById<TextView>(R.id.content).text = "Profile anda gagal diperbaharui, periksa kembali data anda"

        dialog.show()
        dialog.window?.attributes = lp

        dialog.findViewById<Button>(R.id.bt_close).setOnClickListener {
            dialog.dismiss()
        }
    }

}
