package com.axat.firebaseauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.axat.firebaseauthentication.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth

    // Google SignIn
    companion object {
        private const val rc_signin: Int = 1
    }

    private lateinit var mGoogleSignInOption: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide Status Bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser


        // Sending User to UserProfile if already login
        Handler().postDelayed({
            if (user != null) {
                startActivity(Intent(this@MainActivity, UserProfile_Activity::class.java))
                finish()
            } else {
                null
            }
        }, 1000)


        // Calling Function to Login With Google
        binding.googleSignInBtn.setOnClickListener {

            googleSignInConfig()

            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, rc_signin)

        }

        // Login using IDP
        binding.loginBtn.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Success!!!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, UserProfile_Activity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Please enter a valid email address and password", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Please enter a valid email address and password", Toast.LENGTH_SHORT).show()
            }
        }

    }




    // Login using Google
    private fun googleSignInConfig(): Unit {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == rc_signin) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SingInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SingInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SingInActivity", exception.toString())
            }
        }
    }


    // After successfully Login of User
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SingInActivity", "signInWithCredential:success")
                    startActivity(Intent(this@MainActivity, UserProfile_Activity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SingInActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun singup(view: View) {
        startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        finish()
    }

}