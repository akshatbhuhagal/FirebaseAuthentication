package com.axat.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.axat.firebaseauthentication.databinding.ActivityUserProfileBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth

class UserProfile_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide Status Bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()


        // Getting details of user
        val currentUser = mAuth.currentUser

        binding.tvName.text = currentUser?.displayName
        binding.tvEmail.text = currentUser?.email

        Glide
            .with(this)
            .load(currentUser?.photoUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.profileImg)



        // Button To Sign or Log Out
        binding.btnSignout.setOnClickListener {

            mAuth.signOut()

            startActivity(Intent(this@UserProfile_Activity, MainActivity::class.java))
            finish()

        }

    }
}