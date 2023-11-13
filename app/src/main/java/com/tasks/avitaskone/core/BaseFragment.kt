package com.tasks.avitaskone.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding



abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    val binding get() = _binding!!

    abstract fun inflateLayout(): T

    protected abstract fun runOnViewCreated()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = inflateLayout()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runOnViewCreated()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}



