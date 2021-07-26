package com.example.mirrorgram

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mirrorgram.adapters.GroupRv
import com.example.mirrorgram.databinding.FragmentGroupBinding
import com.example.mirrorgram.databinding.MyDialogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GroupFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var rv: GroupRv
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)
        val binding = FragmentGroupBinding.bind(view)
        val list = ArrayList<String>()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("group")
        rv = GroupRv(list,object :GroupRv.MyInterfaceListener{
            override fun itemClick(str: String) {
                val bundle = Bundle().apply {
                    putString("name",str)
                }
                findNavController().navigate(R.id.groupChatFragment,bundle)
            }
        })
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val children = snapshot.children
                children.forEach {
                    val value = it.getValue(String::class.java)
                    if (value != null) {
                        list.add(value)
                    }
                }
                binding.rv.adapter = rv
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        binding.add.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogView = layoutInflater.inflate(R.layout.my_dialog,null,false)

            dialog.setView(dialogView)
            val dialogBind = MyDialogBinding.bind(dialogView)
            dialog.show()
            dialogBind.save.setOnClickListener {
                val name = dialogBind.et.text.toString()
                if (name.trim().isNotEmpty()){
                    var isHave = true
                    list.forEach {
                        if (it == name){
                            isHave = false
                        }
                    }
                    if (isHave){
                        val key = reference.push().key
                        reference.child(key!!).setValue(name)
                        rv.notifyItemInserted(list.size)
                        dialog.dismiss()
                    }else{
                        Toast.makeText(requireContext(), "siz kiritgan guruh qoshilgan!!!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "malumot toliq kirtilmagan!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.rv.adapter = rv
        return view
    }
}