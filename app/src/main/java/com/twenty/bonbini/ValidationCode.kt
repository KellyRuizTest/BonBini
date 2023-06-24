package com.twenty.bonbini


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_validation_code.*
import java.util.concurrent.TimeUnit

class ValidationCode : AppCompatActivity() {

    var infoNombre : String? = null
    var infoEmail : String? = null
    var infoPass : String? = null
    var infoTel : String? = null

    var codeBySystem : String = ""
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validation_code)

        auth = FirebaseAuth.getInstance()
        infoNombre = intent.getStringExtra("Nombre").toString()
        infoEmail = intent.getStringExtra("Email").toString()
        infoPass = intent.getStringExtra("Password").toString()
        infoTel = intent.getStringExtra("Telefono").toString()

        Toast.makeText(this, "Please see: "+infoTel, Toast.LENGTH_LONG).show()

        sendVerificationCodetoUser(infoTel!!)

        go_to_confirm_login.setOnClickListener {
            val codereceive : String = pinfromUser.text.toString()
            if (!codereceive.isEmpty()){
                verifyCode(codereceive)
            }
        }

    }

    private fun sendVerificationCodetoUser(tel: String) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                codeBySystem = p0
                println("<<======================"+ codeBySystem + "==========================>")
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val code = p0.smsCode
                if (code != null){
                    pinfromUser.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@ValidationCode, p0.message, Toast.LENGTH_LONG).show()
                println(p0.message)

            }

        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            tel,        // Phone number to verify
            60,                 // Timeout duration
            TimeUnit.SECONDS,   // Unit of timeout
            TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
            callbacks);        // OnVerificationStateChangedCallbacks

    }

    private fun verifyCode(codeIn: String) {

        val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(codeBySystem, codeIn)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    // Toast.makeText(this, "Validation complete", Toast.LENGTH_SHORT).show()
                    val userID = auth.currentUser!!.uid
                    val userRef: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("Users")
                    val userMap = HashMap<String, Any>()
                    userMap["pid"] = userID
                    userMap["name"] = infoNombre!!
                    userMap["email"] = infoEmail!!
                    userMap["telefono"] = infoTel!!
                    userMap["password"] = infoPass!!
                    userRef.child(userID).setValue(userMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val to_code = Intent(this, MainActivity::class.java)
                            startActivity(to_code)
                            finish()
                        } else {
                            println("*******************Error creating account****************************")
                        }
                    }
                }

                else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Error Validando "+ "", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}