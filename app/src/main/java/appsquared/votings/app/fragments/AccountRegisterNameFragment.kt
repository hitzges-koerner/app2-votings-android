package appsquared.votings.app.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.votings.android.databinding.FragmentAccountRegisterNameBinding
import appsquared.votings.app.AccountRegisterActivity
import appsquared.votings.app.FragmentInteractionListener
import appsquared.votings.app.Constant

class AccountRegisterNameFragment : Fragment() {

    private var mListener: FragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentAccountRegisterNameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountRegisterNameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextAccountRegisterNameFirst.setText((activity as AccountRegisterActivity).getFirstName())
        binding.editTextAccountRegisterNameLast.setText((activity as AccountRegisterActivity).getLastName())

        binding.editTextAccountRegisterNameFirst.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textViewAccountRegisterNameFirstError.visibility = View.INVISIBLE
                (activity as AccountRegisterActivity).setFirstName(binding.editTextAccountRegisterNameFirst.text.toString())
            }
        })

        binding.editTextAccountRegisterNameLast.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textViewAccountRegisterNameLastError.visibility = View.INVISIBLE
                (activity as AccountRegisterActivity).setLastName(binding.editTextAccountRegisterNameLast.text.toString())
            }
        })

        binding.buttonCardViewAccountRegisterNameNext.bindingButtonCardView.materialCardView.setOnClickListener {
            if(binding.editTextAccountRegisterNameFirst.text.isEmpty()) {
                binding.textViewAccountRegisterNameFirstError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if(binding.editTextAccountRegisterNameLast.text.isEmpty()) {
                binding.textViewAccountRegisterNameLastError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            onButtonPressed(Constant.NEXT)
        }

    }

    fun onButtonPressed(action: Int) {
        if (mListener != null) {
            mListener!!.fragmentInteraction(action, this.javaClass)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = AccountRegisterNameFragment()
    }
}