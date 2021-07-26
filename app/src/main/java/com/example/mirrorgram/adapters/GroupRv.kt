package com.example.mirrorgram.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirrorgram.R
import com.example.mirrorgram.databinding.ItemGroupBinding

class GroupRv(var list:ArrayList<String>,var myInterfaceListener: MyInterfaceListener) :RecyclerView.Adapter<GroupRv.Vh>(){
    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(str:String){
            val bind = ItemGroupBinding.bind(itemView)
            bind.tv.setOnClickListener {
                myInterfaceListener.itemClick(str)
            }
            bind.tv.text = str
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.item_group,parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface MyInterfaceListener{
        fun itemClick(str: String)
    }
}