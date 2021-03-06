package com.example.mymessenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.mymessenger.Adapters.HomeRecAdapter
import com.example.mymessenger.Models.UserModel
import com.example.mymessenger.template.Animator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var recAdapter: HomeRecAdapter
    private lateinit var loader: LottieAnimationView
    private lateinit var animator: Animator
    private lateinit var fadeIn: Animation
    private lateinit var fadeOut: Animation
    private lateinit var s: String

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        firebaseUser = mAuth.currentUser!!
        loader = view.findViewById(R.id.anim_preload2)
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        animator = Animator(loader)
        animator.setPrimAnim(fadeIn, fadeOut)
        animator.loadPrimAnimation(View.VISIBLE)
        recyclerView = view.findViewById(R.id.home_rec)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(context)
        animator.setRefVIew(recyclerView)
        val list = ArrayList<UserModel>()
        database.getReference("users").child(firebaseUser.uid).child("username").get()
            .addOnCompleteListener {
                s = it.result.value.toString()
                list.clear()
                database.getReference("users").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            val user = snap.getValue(UserModel::class.java)
                            if (user != null) {
                                if (!user.username.equals(s)) {
                                    list.add(user)
                                }
                            }
                        }

                        animator.loadPrimAnimation(View.INVISIBLE)
                        recAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, 500).show()
                    }
                })
            }

        recAdapter = HomeRecAdapter(list, context)
        recyclerView.adapter = recAdapter


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}