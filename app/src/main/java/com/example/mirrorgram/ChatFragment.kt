package com.example.mirrorgram

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mirrorgram.adapters.ChatRv
import com.example.mirrorgram.adapters.RvAdapter
import com.example.mirrorgram.databinding.FragmentChatBinding
import com.example.mirrorgram.models.Message
import com.example.mirrorgram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var chatRv: ChatRv
    override fun onStart() {
        super.onStart()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference("users")
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val user = User(
            currentUser?.email,
            currentUser?.displayName,
            currentUser?.phoneNumber,
            currentUser?.photoUrl.toString(),
            currentUser?.uid,
            true,
        )
        reference.child(currentUser?.uid!!).setValue(user)
    }
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_chat, container, false)
        val binding = FragmentChatBinding.bind(view)
        var user = arguments?.getSerializable("user") as User
        firebaseAuth = FirebaseAuth.getInstance()
        binding.iv.setOnClickListener {
            findNavController().popBackStack()
        }
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("myMessage")
        Picasso.get().load(Uri.parse(user.photoUri)).into(binding.ivProfile)
        binding.tvDisplayName.text = user.displayName
        val reference2 = firebaseDatabase.getReference("users")
        reference2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach {
                    val value = it.getValue(User::class.java)
                    if (value != null && user.uid == value.uid) {
                        user = value
                    }
                }
                if (user.isHave!!){
                    binding.online.text = "Online"
                }else{
                    binding.online.text = "Offline"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.ivSend.setOnClickListener {
            val messageEt = binding.etSend.text.toString()
            val simpleDataFormat = SimpleDateFormat("HH:mm")
            val date = simpleDataFormat.format(Date())
            if (messageEt.trim().isNotEmpty()) {
                val key = reference.push().key
                val message = Message(
                    firebaseAuth.currentUser?.uid,
                    user.uid,
                    messageEt,
                    date,
                    firebaseAuth.currentUser?.displayName
                )
                reference.child("${firebaseAuth.currentUser?.uid}/${user.uid}/$key")
                    .setValue(message)
                reference.child("${user.uid}/${firebaseAuth.currentUser?.uid}/$key")
                    .setValue(message)
                binding.etSend.text.clear()
            }
        }
        val list = ArrayList<Message>()
        reference.child("${firebaseAuth.currentUser?.uid}/${user.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    val children = snapshot.children
                    children.forEach {
                        list.add(it.getValue(Message::class.java)!!)
                    }
                    chatRv.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        chatRv = ChatRv(list, firebaseAuth.uid!!)
        binding.rvChat.adapter = chatRv
        return view
    }

}