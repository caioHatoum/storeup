package com.app.storeupp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    //referencia de dados
    private var mDatabaseReference: DatabaseReference? = null
    private var mDataBase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private val TAG = "RegisterActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val msgErrPassMatch:String=getString(R.string.errMsgPassRepeat)
        val msgErrEmailFormat:String=getString(R.string.errMsgEmailFormat)
        val msgErrPasswordLenght:String = getString(R.string.errMsgPassLenght)

        setContentView(R.layout.activity_register)



        mDataBase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDataBase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()



        btnSendRegister!!.setOnClickListener {

            if (!isEmailValid(edTxtEmail.text!!.toString())) {

                msgErr.text = msgErrEmailFormat

            }else if(edTextPassword.text!!.toString().length<8){
                msgErr.text = msgErrPasswordLenght
            }else if(!isMatchPassword(edTextPassword.text!!.toString(), edTextPasswordRepeat.text!!.toString())){

                msgErr.text = msgErrPassMatch;

            }else{
                registerUser()
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun registerUser(){

        mAuth!!
            .createUserWithEmailAndPassword(edTxtEmail.text!!.toString(),edTextPassword.text!!.toString()).addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d(TAG,"CreateUserWithEmail:Success")

                    val userId = mAuth!!.currentUser!!.uid

                    verifyEmail()

                    val currentUserDb = mDatabaseReference!!.child(userId)
                    currentUserDb.child("email").setValue(edTxtEmail.text!!.toString())
                    currentUserDb.child("password").setValue(edTextPassword.text!!.toString())

                    updateUserInfoAndUi()
                }else{
                    Log.w(TAG,"not sucessful create user: failure",task.exception)
                    Toast.makeText(this@RegisterActivity,"a autenticação falhou",Toast.LENGTH_SHORT)
                }
            }

    }
    private fun updateUserInfoAndUi(){
        val intent = Intent(this@RegisterActivity,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verifyEmail(){
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this){
            task ->

            if(task.isSuccessful){
                Toast.makeText(this@RegisterActivity,"verificação enviada para :"+mUser.email,Toast.LENGTH_SHORT).show()
            }else{
                Log.e(TAG,"SendEmailVerification", task.exception)
                Toast.makeText(this@RegisterActivity, "Falha na verificação do email", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isMatchPassword(pass:String, repeat:String):Boolean{
        return pass==repeat
    }
}
