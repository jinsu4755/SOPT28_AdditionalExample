package com.nunu.viewbindingsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nunu.viewbindingsample.base.viewBinding
import com.nunu.viewbindingsample.databinding.FragmentBindingDelegateBinding

class BindingDelegateFragment : Fragment() {
    private val binding by viewBinding<FragmentBindingDelegateBinding>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}