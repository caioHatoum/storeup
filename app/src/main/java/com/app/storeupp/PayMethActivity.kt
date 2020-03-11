package com.app.storeupp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stripe.android.PaymentConfiguration
import kotlinx.android.synthetic.main.activity_pay_meth.*

class PayMethActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_meth)
        btnCardRegister.setOnClickListener {

        }
    }
}
