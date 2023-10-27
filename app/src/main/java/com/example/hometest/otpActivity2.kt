package com.example.hometest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class otpActivity2 : AppCompatActivity() {

    val auth= FirebaseAuth.getInstance()
    private lateinit var verificationid:String
    private  lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp2)
        val otp=findViewById<EditText>(R.id.pin)
        val enter=findViewById<Button>(R.id.VERFY)
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Loding...")
        builder.setMessage("Please Wait...")

        builder.setCancelable(false)
        dialog=builder.create()

        dialog.show()
        val phonenumber=intent.getStringExtra("number")
        val PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phonenumber")
            .setTimeout(90L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@otpActivity2, p0.message, Toast.LENGTH_SHORT).show()
                }
                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    verificationid=p0
                }
            })


            .setActivity(this)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions)

        enter.setOnClickListener {
            if (otp.text.isEmpty()){
                Toast.makeText(this, "Enter the otp", Toast.LENGTH_SHORT).show()
            }
            else{
                dialog.show()


                val credential= PhoneAuthProvider.getCredential(verificationid,otp.text!!.toString())
                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                        chek()
//                        val intent= Intent(this,addActivity::class.java)
//                        startActivity(intent)

                        dialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "try agin", Toast.LENGTH_SHORT).show()
                    }
            }

        }
    }
    fun chek(){
        val db= Firebase.firestore.collection("teacher")  .whereEqualTo("userid",auth.currentUser?.uid.toString()).get()

            .addOnSuccessListener {
                if (it.isEmpty){
                    val intent= Intent(this,registerActivity2::class.java)
                    startActivity(intent)
                }
                else{
                    val intent= Intent(this,homeActivity2::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
            }

    }
}