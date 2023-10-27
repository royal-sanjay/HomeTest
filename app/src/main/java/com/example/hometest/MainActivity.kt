package com.example.hometest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    val auth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val number=findViewById<EditText>(R.id.number)
        val buttom=findViewById<Button>(R.id.sendotp)

        buttom.setOnClickListener {
            if (number.text.isEmpty()){
                Toast.makeText(this, "Enter the number", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent= Intent(this,otpActivity2::class.java)
                intent.putExtra("number",number.text!!.toString())
                startActivity(intent)
            }
        }

    }
}