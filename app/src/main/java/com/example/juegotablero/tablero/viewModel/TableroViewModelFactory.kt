package com.example.juegotablero.tablero.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TableroViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TableroViewModel::class.java)) {
            return TableroViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}