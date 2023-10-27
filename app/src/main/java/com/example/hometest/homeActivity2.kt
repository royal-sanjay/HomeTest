package com.example.hometest

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class homeActivity2 : AppCompatActivity() {

    var auth=FirebaseAuth.getInstance()
    //val db=Firebase.firestore.collection("users")
    lateinit var db: CollectionReference

    //    val db=Firebase.firestore.collection("user")
    lateinit var uploadImageBtn: Button
    lateinit var imageView: ImageView
    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var adderss:EditText
    var fileUri: Uri? = null;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)
         name=findViewById<EditText>(R.id.Name)
        email=findViewById<EditText>(R.id.Email)
       adderss=findViewById<EditText>(R.id.Adderss)
        val logout=findViewById<Button>(R.id.logout)
        val toolbar=findViewById<Toolbar>(R.id.tolbar)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.Emaill->{
                    email()
                    Toast.makeText(this, "email", Toast.LENGTH_SHORT).show()
                }
                R.id.Phonee->{
                    val intent=Intent(Intent.ACTION_DIAL)
                 startActivity(intent)
                    return@setOnMenuItemClickListener  true

                }
                R.id.logoutt->{
                    val alertDialog=AlertDialog.Builder(this)

                    alertDialog.setPositiveButton("yes",DialogInterface.OnClickListener { dialogInterface, i ->
                        auth.signOut()
                        val intent= Intent(this,MainActivity::class.java)

                        startActivity(intent)
                    })
                    alertDialog.setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->
                        finish()
                    })

                    val dialog=alertDialog.create()
                    dialog.setTitle("Welcome")
                    dialog.setMessage("you are  sure Logout ")
                    dialog.show()
                }
                R.id.Deletee->{
                  val alertDialog=AlertDialog.Builder(this)

                    alertDialog.setPositiveButton("yes",DialogInterface.OnClickListener { dialogInterface, i ->
                        auth.currentUser?.delete()
                        val intent= Intent(this,MainActivity::class.java)

                        startActivity(intent)
                    })
                    alertDialog.setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->
                        finish()
                    })

                    val dialog=alertDialog.create()
                    dialog.setTitle("Welcome")
                    dialog.setMessage("you are  sure Account Delete")
                   dialog.show()


                }
            }
            true
        }
//        logout.setOnClickListener {
//            auth.signOut()
//            Toast.makeText(this, "Logout Successfuly", Toast.LENGTH_SHORT).show()
//            val intent= Intent(this,MainActivity::class.java)
//            startActivity(intent)
//        }
        val collection = Firebase.firestore.collection("teacher")
        val userId = auth.currentUser?.uid
        if (userId != null) {
            collection.document(userId).get()
                .addOnSuccessListener {
                    val data=it.toObject(TeacherModel::class.java)
                     name.setText(data?.name)
                    email.setText(data?.email)
                    adderss.setText(data?.adderss)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
        }






        val imagr = findViewById<CircleImageView>(R.id.profile_image)
        uploadImageBtn = findViewById(R.id.logout)
        imageView = findViewById(R.id.profile_image)

        imagr.setOnClickListener {

            val intent = Intent()

            intent.type = "image/*"

            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(

                Intent.createChooser(
                    intent,
                    "Pick your image to upload"
                ),
                22
            )
        }
        uploadImageBtn.setOnClickListener {

            uploadImage()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.data != null) {

            fileUri = data.data
            try {

                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri);

                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
        if(requestCode== CAMERA_REQUST && resultCode== Activity.RESULT_OK){
            val cPhoto=data!!.extras?.get("data")
            imageView.setImageBitmap(cPhoto as Bitmap?)
        }

    }
    companion object{
        private   val CAMERA_REQUST=200
    }
    fun uploadImage() {

        if (fileUri != null) {

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())

            ref.putFile(fileUri!!).addOnSuccessListener {task->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Image Uploaded..", Toast.LENGTH_SHORT).show()
                uploadinfo(task.toString())
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "rinki", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    private fun uploadinfo(imageUri:String){
        val userid=auth.currentUser?.uid
//     val db=
        val collection=Firebase.firestore.collection("teacher")

        val user=TeacherModel(name.text.toString(),
            email.text.toString(),
            adderss.text.toString(),
            imageUri,
            auth.uid.toString())
        if (userid != null) {
            collection.document(userid).set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "data insert", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, chatActivity::class.java)
//                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "err", Toast.LENGTH_SHORT).show()
                }
        }


    }
           fun email(){

            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("Email")
            val str_Arry = arrayOf("test1@gmail.com","test2@gmail.com","test3@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL,str_Arry)
            intent.putExtra(Intent.EXTRA_SUBJECT,"This is for subject")
            intent.putExtra(Intent.EXTRA_TEXT,"this field is Email body")
            intent.type = "Message/rfc822"
            val a = Intent.createChooser(intent,"Launch Email")
            startActivity(a)
        }


    }



