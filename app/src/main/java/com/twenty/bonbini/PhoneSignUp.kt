package com.twenty.bonbini

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_phone_sign_up.*

class PhoneSignUp : AppCompatActivity() {

    private var email : String? =null
    private var name : String? = null
    private var password: String? = null
    private var telefono: String? = null
    private lateinit var auth: FirebaseAuth

  //  private lateinit var cpp : CountryCodePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_sign_up)

        auth = FirebaseAuth.getInstance()
        name = intent.getStringExtra("Nombre").toString()
        email = intent.getStringExtra("Email").toString()
        password = intent.getStringExtra("Password").toString()


        var countrCode = countryCodePicker2.selectedCountryCodeWithPlus.toString()
        Toast.makeText(this, countrCode+"", Toast.LENGTH_SHORT).show()


        next_to_verify_code.setOnClickListener {

            if (!phone_login.text.toString().isEmpty()){
                telefono = phone_login.text.toString()
                var userName : String = name.toString()
                var userEmail : String = email.toString()
                var userPassword : String = password.toString()

                //println("---------------->"+countryCodePicker2!!.fullNumber+"<---------------------")

                var userTelefono : String = countrCode+telefono.toString()
                //var userTelefono : String = "+"+countryCodePicker2.fullNumber+telefono.toString()

                // Registration with Validation OTP
                val intentTocheckOTP = Intent(this, ValidationCode::class.java)
                intentTocheckOTP.putExtra("Nombre", userName)
                intentTocheckOTP.putExtra("Email", userEmail)
                intentTocheckOTP.putExtra("Password", userPassword)
                intentTocheckOTP.putExtra("Telefono", userTelefono)
                startActivity(intentTocheckOTP)

                // Registration Working withoud Validation OTP

                /*
                val userNode : FirebaseAuth = FirebaseAuth.getInstance()
                val progressTask = ProgressDialog(this)
                progressTask.setTitle("Registro")
                progressTask.setMessage("Por favor espere")
                progressTask.setCanceledOnTouchOutside(false)
                progressTask.show()

                auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        val userID = auth.currentUser!!.uid
                        val userRef : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
                        val userMap = HashMap<String, Any>()
                        userMap["pid"] = userID
                        userMap["name"] = userName
                        userMap["email"] = userEmail
                        userMap["telefono"] = userTelefono
                        userMap["password"] = userPassword

                        userRef.child(userID).setValue(userMap).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                progressTask.dismiss()
                                val to_code = Intent(this, MainActivity::class.java)
                                //to_code.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(to_code)
                                finish()
                            }else{
                                println("*******************Error creating account****************************")
                            }
                        }
                    }else{
                        Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show()
                        println("<============>"+task.exception!!.message)
                        userNode.signOut()
                        progressTask.dismiss()
                    }
                }*/
            }else{

                Toast.makeText(this, "Debes poner un telefono!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}