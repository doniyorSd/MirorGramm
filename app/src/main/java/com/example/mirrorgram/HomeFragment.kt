package com.example.mirrorgram

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.mirrorgram.adapters.ViewPagerAdapter
import com.example.mirrorgram.databinding.FragmentHomeBinding
import com.example.mirrorgram.databinding.TabItemBinding
import com.example.mirrorgram.models.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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
    lateinit var firebaseFireStore:FirebaseFirestore
    lateinit var viewpager:ViewPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val binding = FragmentHomeBinding.bind(view)
        firebaseFireStore = FirebaseFirestore.getInstance()
        val user = User("12","12","12","12","12",false)
        firebaseFireStore.collection("users").add(user)
        viewpager = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = viewpager
        TabLayoutMediator(binding.tabLayout,binding.viewPager) { tab, position ->
            val inflate =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_item, null, false)
            tab.customView = inflate
            val bind = TabItemBinding.bind(inflate)
            if (position == 0) {
                bind.tv.text = "Chats"
                bind.tab.setCardBackgroundColor(Color.parseColor("#2675EC"))
                bind.tv.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                bind.tv.text = "Groups"
                bind.tab.setCardBackgroundColor(Color.parseColor("#E5E5E5"))
                bind.tv.setTextColor(Color.parseColor("#848484"))
            }
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val bind = TabItemBinding.bind(customView!!)
                bind.tab.setCardBackgroundColor(Color.parseColor("#2675EC"))
                bind.tv.setTextColor(Color.parseColor("#FFFFFF"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                val bind = TabItemBinding.bind(customView!!)
                bind.tab.setCardBackgroundColor(Color.parseColor("#E5E5E5"))
                bind.tv.setTextColor(Color.parseColor("#848484"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        binding.exit.setOnClickListener {
            findNavController().popBackStack()
        }
        return view
    }

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
}