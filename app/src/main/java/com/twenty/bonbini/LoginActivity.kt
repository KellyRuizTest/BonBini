package com.twenty.bonbini

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.twenty.bonbini.Model.Users
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {


    private var phoneLogin : TextView? = null
    private var passLogin : TextView? = null

    private lateinit var countryCodePick : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        countryCodePick = country_code_picker.selectedCountryCodeWithPlus.toString()

        login_account.setOnClickListener {
            LoginUser()
            //val goto = Intent(applicationContext, MainActivity::class.java)
            //startActivity(goto)
        }
        registration_button.setOnClickListener {
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

        var _phone : String = countryCodePick+edit_email_login.text.toString().trim()
        var _pass : String = edit_pass_login.text.toString().trim()

        if (_phone.get(0) == '0'){
            _phone = _phone.substring(1)
        }

        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                var aux: Boolean = false
                var passFrom: String? = null
                for (snap in p0.children) {
                    val useraux = snap.getValue(Users::class.java)

                    if (useraux!!.getTelefono() == _phone) {
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

                        val userAuth : FirebaseAuth = FirebaseAuth.getInstance()


                        relative_progress.visibility = View.GONE

                    } else {
                        relative_progress.visibility = View.GONE
                        Toast.makeText(
                            this@LoginActivity,
                            "Password no es correcto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    relative_progress.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Usuario no existe", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun showDialogConnection() {

        val builder : AlertDialog.Builder = this@LoginActivity.let {
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

        if (networkInfo != null && isConnected){
            return true
        }else {
            return false
        }
    }

    private fun validateFiels(): Boolean {

        var flaggi : Boolean = false
        val _phone : String = edit_email_login.text.toString().trim()
        val _pass : String = edit_pass_login.text.toString().trim()

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
}