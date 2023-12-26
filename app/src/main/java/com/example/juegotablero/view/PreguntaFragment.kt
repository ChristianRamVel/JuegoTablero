package com.example.juegotablero.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.juegotablero.R
import com.example.juegotablero.viewModel.PreguntaViewModel

class PreguntaFragment : Fragment() {

    companion object {
        fun newInstance() = PreguntaFragment()
    }

    private lateinit var viewModel: PreguntaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pregunta, container, false)
    }


}