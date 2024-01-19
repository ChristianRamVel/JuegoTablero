package com.example.juegotablero.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.juegotablero.R
import com.example.juegotablero.model.Partida

class PartidasAdapter(context: Context, resource: Int, private val partidas: List<Partida>) :
    ArrayAdapter<Partida>(context, resource, partidas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_partida_guardada, parent, false)

        val partida = getItem(position)

        val nombreTextView = itemView.findViewById<TextView>(R.id.nombreTextView)
        val fechaTextView = itemView.findViewById<TextView>(R.id.fechaTextView)

        nombreTextView.text = "Partida ${partida?.id}"
        fechaTextView.text = partida?.fecha




        return itemView
    }
}