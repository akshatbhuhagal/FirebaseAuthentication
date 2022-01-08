package com.axat.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.axat.firebaseauthentication.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Hide Status Bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        supportActionBar?.hide()


        auth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account registered successfully", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(this, "Failed! Try Again", Toast.LENGTH_SHORT).show()
            }

        }





    }

    fun login(view: View) {

        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}