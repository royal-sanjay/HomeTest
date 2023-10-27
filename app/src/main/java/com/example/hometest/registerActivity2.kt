package com.example.hometest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class registerActivity2 : AppCompatActivity() {
    val auth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        val name=findViewById<EditText>(R.id.name)
        val email=findViewById<EditText>(R.id.email)
        val adderss=findViewById<EditText>(R.id.adderss)
        val regisret=findViewById<Button>(R.id.register)
        regisret.setOnClickListener {
            if (name.text.isEmpty()){
                Toast.makeText(this, "Enter the name", Toast.LENGTH_SHORT).show()
            }
            else if (email.text.isEmpty()){
                Toast.makeText(this, "Enter the email", Toast.LENGTH_SHORT).show()
            }
            else if (adderss.text.isEmpty()){
                Toast.makeText(this, "Enter the adderss", Toast.LENGTH_SHORT).show()
            }
            else{
                val userId=auth.currentUser?.uid
                val collection=Firebase.firestore.collection("teacher")
                 val teacher=TeacherModel(
                 name.text.toString(),
                     email.text.toString(),
                     adderss.text.toString(),
                     userId.toString()


                 )
                if (userId != null) {
                    collection.document(userId).set(teacher)
                        .addOnSuccessListener {
                            Toast.makeText(this, "data insert", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, homeActivity2::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}