package com.example.mirrorgram.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mirrorgram.R
import com.example.mirrorgram.databinding.ItemUserBinding
import com.example.mirrorgram.models.SecondUser
import com.example.mirrorgram.models.User
import com.squareup.picasso.Picasso

class RvAdapter(var list:ArrayList<SecondUser>,var onMyClickListener: OnMyClickListener) :RecyclerView.Adapter<RvAdapter.Vh>(){
    inner class Vh(itemView:View) :RecyclerView.ViewHolder(itemView){
        fun onBind(user: SecondUser){
           val bind =  ItemUserBinding.bind(itemView)
            if (user.photoUri != null){
                Picasso.get().load(Uri.parse(user.photoUri)).into(bind.iv)
            }

            bind.name.text = user.displayName
            if (user.time != "" && user.displayName != null){
                bind.time.text = user.time
                bind.message.text = user.lastMessage
            }

            if (user.isHave!!){
                bind.viewOnline.visibility = View.VISIBLE
                bind.viewOffline.visibility = View.GONE
            }else{
                bind.viewOnline.visibility = View.GONE
                bind.viewOffline.visibility = View.VISIBLE
            }
            itemView.setOnClickListener {
                val user2 = User(user.email,user.displayName,user.phoneNumber,user.photoUri,user.uid,user.isHave)
                onMyClickListener.onClick(user2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnMyClickListener{
        fun onClick(user:User)
    }
}