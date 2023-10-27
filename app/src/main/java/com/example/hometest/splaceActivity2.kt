package com.example.hometest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class splaceActivity2 : AppCompatActivity() {
    val auth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splace2)
        val userid=auth.currentUser?.uid
        Handler().postDelayed({
                if (userid==null){
                   val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
            else {
                    val intent= Intent(this,homeActivity2::class.java)
                    startActivity(intent)
                }
        },2000)
    }
}