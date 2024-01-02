package com.example.juegotablero.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.juegotablero.R
import com.example.juegotablero.viewModel.ParejasViewModel

class ParejasFragment : Fragment() {

    private lateinit var viewModel: ParejasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parejas, container, false)
    }

}