package com.twenty.bonbini.Model

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.twenty.bonbini.MainActivity
import com.twenty.bonbini.R
import com.twenty.bonbini.RegistrationActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.login_account
import kotlinx.android.synthetic.main.activity_login.registration_button
import kotlinx.android.synthetic.main.activity_login.relative_progress
import kotlinx.android.synthetic.main.activity_login.textInputLayout
import kotlinx.android.synthetic.main.activity_login.textInputLayout2
import kotlinx.android.synthetic.main.activity_login_new.*

class LoginNewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_new)

        login_account_new.setOnClickListener {
            LoginUser()
        }
        registration_button_new.setOnClickListener {
            val gotoRegistration = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(gotoRegistration)
        }
    }

    private fun LoginUser() {

        if(!isConnected()){
            showDialogConnection()
        }

        if (validateFiels()){
            return
        }

        relative_progress.visibility = View.VISIBLE

        var _email : String = email_login.text.toString().trim()
        var _pass : String = pass_login.text.toString().trim()


        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                var aux: Boolean = false
                var passFrom: String? = null
                for (snap in p0.children) {
                    val useraux = snap.getValue(Users::class.java)

                    if (useraux!!.getEmail() == _email) {
                        val ppty = useraux
                        aux = true
                        passFrom = ppty.getPassword()
                    }
                }

                if (aux) {
                    textInputLayout.setError(null)
                    textInputLayout.setErrorEnabled(false)
                    val systemPassword: String = passFrom!!

                    if (systemPassword.equals(_pass)) {
                        textInputLayout2.setError(null)
                        textInputLayout2.setErrorEnabled(false)

                        //signIn
                        val userAuth : FirebaseAuth = FirebaseAuth.getInstance()
                        userAuth.signInWithEmailAndPassword(_email, _pass).addOnCompleteListener { task ->

                            if (task.isSuccessful){
                                relative_progress.visibility = View.GONE
                                Toast.makeText(this@LoginNewActivity, "Logged successfully", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }else {
                                Toast.makeText(this@LoginNewActivity, "Failed "+ task.exception, Toast.LENGTH_SHORT).show()
                                FirebaseAuth.getInstance().signOut()
                                relative_progress.visibility = View.GONE
                            }

                        }

                    } else {
                        relative_progress.visibility = View.GONE
                        Toast.makeText(
                            this@LoginNewActivity,
                            "Password no es correcto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    relative_progress.visibility = View.GONE
                    Toast.makeText(this@LoginNewActivity, "Usuario no existe", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun showDialogConnection() {

        val builder : AlertDialog.Builder = this@LoginNewActivity.let {
            AlertDialog.Builder(it)
        }

        builder.setMessage("No estas conectado a Internet")
        builder.setPositiveButton("Conectar", DialogInterface.OnClickListener { dialogInterface, i ->
            startActivity(Intent(Settings.ACTION_WIFI_IP_SETTINGS))
        })

        builder.create()

    }

    private fun isConnected(): Boolean {

        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo : NetworkInfo? = connectivityManager.activeNetworkInfo

        val isConnected : Boolean = networkInfo?.isConnected == true

        return networkInfo != null && isConnected
    }

    private fun validateFiels(): Boolean {

        var flaggi : Boolean = false
        val _phone : String = email_login.text.toString().trim()
        val _pass : String = pass_login.text.toString().trim()

        if (TextUtils.isEmpty(_phone)){
            flaggi = true
            textInputLayout.setError("no puede ser vacio")
            textInputLayout.setErrorEnabled(true)
        }

        if (TextUtils.isEmpty(_pass)){
            flaggi = true
            textInputLayout2.setError("no puede ser vacio")
            textInputLayout2.setErrorEnabled(true)
        }

        return flaggi
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }

    }
}