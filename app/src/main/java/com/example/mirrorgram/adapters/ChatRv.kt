package com.example.mirrorgram.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirrorgram.R
import com.example.mirrorgram.databinding.ItemFromBinding
import com.example.mirrorgram.databinding.ItemToBinding
import com.example.mirrorgram.models.Message

class ChatRv(var list: ArrayList<Message>, var uid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class FromVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(message: Message){
            val bind = ItemFromBinding.bind(itemView)
            if (message.name != null){
                bind.tv.text = message.message
                bind.date.text = message.date
                bind.name.text = message.name
            }
        }
    }

    inner class ToVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(message: Message){
            val bind = ItemToBinding.bind(itemView)
            if (message.name != null){
                bind.tv.text = message.message
                bind.date.text = message.date
                bind.name.text = message.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return FromVh(
                LayoutInflater.from(parent.context).inflate(R.layout.item_from, parent, false)
            )
        } else {
            return ToVh(
                LayoutInflater.from(parent.context).inflate(R.layout.item_to, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1){
            val fromVh =   holder as FromVh
            fromVh.onBind(list[position])
        }else{
            val fromVh =   holder as ToVh
            fromVh.onBind(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int = if (list[position].fromId == uid) {
        1
    } else {
        2
    }
}