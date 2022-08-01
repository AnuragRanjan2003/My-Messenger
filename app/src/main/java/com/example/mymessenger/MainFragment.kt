package com.example.mymessenger

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.mymessenger.Adapters.PostRecAdapter
import com.example.mymessenger.Models.PostModel
import com.example.mymessenger.template.Animator
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recView: RecyclerView
    private lateinit var compose: ExtendedFloatingActionButton
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fUser: FirebaseUser
    private lateinit var recAdapter: PostRecAdapter
   private lateinit var preLoader:LottieAnimationView

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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recView = view.findViewById(R.id.main_rec)
        compose = view.findViewById(R.id.fab_compose)
        databaseReference = FirebaseDatabase.getInstance().getReference("posts")
        mAuth = FirebaseAuth.getInstance()
        fUser = mAuth.currentUser!!
        val postList = ArrayList<PostModel>()
        preLoader=view.findViewById(R.id.anim_preload3)
        preLoader.visibility=1;
        recView.layoutManager = LinearLayoutManager(context)
        recView.hasFixedSize()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val pm = snap.getValue(PostModel::class.java)
                    if (!pm?.senderUid.equals(fUser.uid)) {
                        postList.add(pm!!)
                    }
                }
                recAdapter.notifyDataSetChanged()
                preLoader.visibility=-1
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        recAdapter = PostRecAdapter(postList, context)
        recView.adapter = recAdapter
        compose.setOnClickListener {
            activity?.startActivity(Intent(context, PostActivity::class.java))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}