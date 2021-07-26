package com.example.mirrorgram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mirrorgram.adapters.ChatRv
import com.example.mirrorgram.databinding.FragmentGroupChatBinding
import com.example.mirrorgram.models.Message
import com.example.mirrorgram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupChatFragment : Fragment() {
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
    lateinit var reference: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_group_chat, container, false)

        val binding = FragmentGroupChatBinding.bind(view)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("groupMessage")
        val name = arguments?.getString("name")
        binding.linear.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvGroup.text = name
        binding.ivSend.setOnClickListener {
            val messageEt = binding.etSend.text.toString()
            val simpleDataFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val date = simpleDataFormat.format(Date())
            if (messageEt.trim().isNotEmpty()) {
                val key = reference.push().key
                val message = Message(firebaseAuth.currentUser?.uid, null, messageEt, date,firebaseAuth.currentUser?.displayName)
                reference.child("${name}/$key").setValue(message)
                binding.etSend.text.clear()
            }
        }
        val list = ArrayList<Message>()
        reference.child("$name").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val children = snapshot.children
                children.forEach {
                    list.add(it.getValue(Message::class.java)!!)
                }
                chatRv.notifyItemInserted(list.size)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        chatRv = ChatRv(list,firebaseAuth.uid!!)
        binding.rv.adapter = chatRv
        return view
    }
}