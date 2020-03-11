package com.app.storeupp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*


private var auth: FirebaseAuth? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        btnForgot.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
        btnRegister!!.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        btnLogin.setOnClickListener {
            signIn(edEmailLogin.text.toString(),edPassLogin.text.toString())
        }


    }
    private fun signIn(email: String, password: String) {

        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }


        // [START sign_in_with_email]
        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth!!.currentUser
                    updateUI(user)
                    startActivity(Intent(this,InitialActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    txtStatus.setText(R.string.auth_failed)
                }
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }
    private fun validateForm(): Boolean {
        var valid = true


        if (TextUtils.isEmpty(edEmailLogin.text.toString())) {
            edEmailLogin.error = "Required."
            valid = false
        } else {
            edEmailLogin.error = null
        }


        if (TextUtils.isEmpty(edPassLogin.text.toString())) {
            edPassLogin.error = "Required."
            valid = false
        } else {
            edPassLogin.error = null
        }

        return valid
    }

    @SuppressLint("StringFormatInvalid")
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            txtStatus.text = getString(R.string.emailpassword_status_fmt,
                user.email, user.isEmailVerified)
            txtDetail.text = getString(R.string.firebase_status_fmt, user.uid)


        } else {
            txtStatus.setText(R.string.signed_out)
            txtDetail.text = null


        }
    }
    companion object {
        private const val TAG = "EmailPassword"
    }

}
