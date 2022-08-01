package com.example.mymessenger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.mymessenger.Models.UserModel
import com.example.mymessenger.template.Animator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var img: ImageView
    private lateinit var fUser: FirebaseUser
    private lateinit var storage: FirebaseStorage
    private lateinit var loader: LottieAnimationView
    private lateinit var u: UserModel
    private lateinit var name: TextView
    private lateinit var place: TextView
    private lateinit var job: TextView
    private lateinit var animator: Animator
    private lateinit var fadeIn: Animation
    private lateinit var fadeOut: Animation
    private val visible = 1
    private val invisible = -1

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
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dRef = FirebaseDatabase.getInstance().getReference("users")
        val mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        fUser = mAuth.currentUser!!
        val logOut = view.findViewById<Button>(R.id.btn_logOut)
        logOut.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(context, AskActivity::class.java)
            startActivity(intent)
        }
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        name = view.findViewById(R.id.set_Name)
        place = view.findViewById(R.id.set_place)
        job = view.findViewById(R.id.set_job)

        loader = view.findViewById(R.id.anim_preload)
        animator = Animator(loader)
        animator.setPrimAnim(fadeIn, fadeOut)
        img = view.findViewById(R.id.set_pImg)
        animator.setRefVIew(img)
        setVis(invisible)
        dRef.child(fUser.uid).get().addOnSuccessListener {
            u = it.getValue(UserModel::class.java)!!
            name.text = u.username
            place.text = u.place
            job.text = u.job
            Glide.with(view.context).load(u.url).into(img)
            setVis(visible)
            animator.loadPrimAnimation(View.INVISIBLE)
        }.addOnFailureListener {
            Toast.makeText(view.context, getString(R.string.noData), Toast.LENGTH_LONG).show()
            setVis(visible)
            name.text = getString(R.string.noData)
            place.text = getString(R.string.noData)
            job.text = getString(R.string.noData)
            animator.loadPrimAnimation(View.INVISIBLE)
        }
        img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val ur = data?.data
        val dRef =
            FirebaseDatabase.getInstance().getReference("users").child(fUser.uid).child("url")
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            setVis(invisible)
            animator.loadPrimAnimation(View.VISIBLE)
            val sRef =
                storage.getReference("profileImages/" + fUser.uid + "." + getFileExtension(ur!!))
            sRef.putFile(ur).addOnSuccessListener {
                Toast.makeText(context, "Changed successfully", Toast.LENGTH_LONG).show()
                sRef.downloadUrl.addOnSuccessListener {
                    dRef.setValue(it.toString()).addOnCompleteListener {
                        animator.loadPrimAnimation(View.INVISIBLE)
                        setVis(visible)
                    }.addOnSuccessListener {
                        dRef.get().addOnCompleteListener { it1 ->
                            Glide.with(view!!.context).load(it1.result.value).into(img)
                        }

                    }
                }
                    .addOnFailureListener {
                        setVis(visible)
                        animator.loadPrimAnimation(View.INVISIBLE)
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
            }
                .addOnFailureListener {
                    setVis(visible)
                    animator.loadPrimAnimation(View.INVISIBLE)
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cr = activity?.contentResolver
        val mMap = MimeTypeMap.getSingleton()
        return mMap.getExtensionFromMimeType(cr!!.getType(uri))
    }

    private fun setVis(x: Int) {
        name.visibility = x
        job.visibility = x
        place.visibility = x
        img.visibility = x
    }


}