package com.app.storeupp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_initial.*

class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        toStay.setOnClickListener {
            startActivity(Intent())
        }
        toStore.setOnClickListener {
            startActivity(Intent())
        }
        myAccount.setOnClickListener {
            startActivity(Intent(this,MyAccountActivity::class.java))
        }
        myStore.setOnClickListener {
            startActivity(Intent())
        }
        logout.setOnClickListener {

        }
    }
}
