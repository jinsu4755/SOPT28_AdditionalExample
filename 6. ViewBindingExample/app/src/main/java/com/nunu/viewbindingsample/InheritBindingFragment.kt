package com.nunu.viewbindingsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nunu.viewbindingsample.base.BindingFragment
import com.nunu.viewbindingsample.databinding.FragmentInheritBindingBinding

class InheritBindingFragment :
    BindingFragment<FragmentInheritBindingBinding>(R.layout.fragment_inherit_binding) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}