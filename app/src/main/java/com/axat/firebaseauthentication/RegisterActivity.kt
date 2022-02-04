package com.axat.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.axat.firebaseauthentication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Hide Status Bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()


        mAuth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account registered successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, "Please enter a valid email address and password", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "Please enter a valid email address and password", Toast.LENGTH_SHORT).show()
            }
        }





    }

    fun login(view: View) {

        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}