package com.example.mirrorgram

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mirrorgram.adapters.RvAdapter
import com.example.mirrorgram.databinding.FragmentUserBinding
import com.example.mirrorgram.models.Message
import com.example.mirrorgram.models.SecondUser
import com.example.mirrorgram.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserFragment : Fragment() {
    lateinit var rvAdapter: RvAdapter
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var binding: FragmentUserBinding
    val list = ArrayList<User>()
    val list2 = ArrayList<SecondUser>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        binding = FragmentUserBinding.bind(view)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val user = User(
            currentUser?.email,
            currentUser?.displayName,
            currentUser?.phoneNumber,
            currentUser?.photoUrl.toString(),
            currentUser?.uid,
            true,
        )
        if (currentUser != null){
            reference.child(currentUser.uid).setValue(user)
        }
        val reference2 = firebaseDatabase.getReference("myMessage")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                list2.clear()
                val children = snapshot.children
                children.forEach {
                    val value = it.getValue(User::class.java)
                    if (value != null && currentUser?.uid != value.uid) {
                        list.add(value)
                        list2.add(SecondUser(value.email,value.displayName,value.phoneNumber,value.photoUri,value.uid,value.isHave))
                    }
                }
                val arr = ArrayList<Message>()
                list2.forEach { us ->
                    reference2.child("${firebaseAuth.currentUser?.uid}/${us.uid}")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val children2 = snapshot.children
                                children2.forEach {
                                    arr.add(it.getValue(Message::class.java)!!)
                                }
                                if (arr.isNotEmpty()){
                                    us.lastMessage = arr.last().message
                                    us.time = arr.last().date
                                    rvAdapter.notifyDataSetChanged()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                }
                rvAdapter = RvAdapter(list2, object : RvAdapter.OnMyClickListener {
                    override fun onClick(user: User) {
                        val bundle = Bundle().apply {
                            putSerializable("user", user)
                        }
                        findNavController().navigate(R.id.chatFragment, bundle)
                    }
                })

                binding.rv.adapter = rvAdapter

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        list2.forEach { us ->
            reference2.child("${firebaseAuth.currentUser?.uid}/${us.uid}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val arr = ArrayList<Message>()
                        val children2 = snapshot.children
                        children2.forEach {
                            arr.add(it.getValue(Message::class.java)!!)
                        }
                        if (arr.isNotEmpty()){
                            us.lastMessage = arr.last().message
                        }
                        rvAdapter.notifyDataSetChanged()
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<User>) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}