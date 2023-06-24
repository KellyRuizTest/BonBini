package com.twenty.bonbini

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    var name : String? = null
    var email : String? = null
    var password : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        privacy_policy_popup()

        register_account_btn.setOnClickListener {
            val intentToCheckPhone = Intent (applicationContext, PhoneSignUp::class.java)

            if( createAccountWithValidations() ){
                intentToCheckPhone.putExtra("Nombre", name)
                intentToCheckPhone.putExtra("Email", email)
                intentToCheckPhone.putExtra("Password", password)

                startActivity(intentToCheckPhone)
            }else {
                return@setOnClickListener
            }
        }
    }

    private fun createAccountWithValidations(): Boolean {

        name = edit_name.text.toString()
        email = edit_email.text.toString()
        password = edit_pass.text.toString()

        return validateName() && validateEmail() && validatePassword()


    }

    private fun validateName(): Boolean {
        val name = edit_name.text.toString()

        if(name.isEmpty()){
            edit_name.error = "Field can not be empty"
            return false
        } else {
            edit_name.error = null
            return true
        }

    }

    private fun validateEmail() : Boolean {
        val email: String = edit_email .text.toString()
        val checkemail = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

        if (email.isEmpty()) {
            edit_email.error = "Field can not be empty"
            return false
        } else if (!email.matches(checkemail)) {
            edit_email.error = "Invalid email address!"
            return false
        } else {
            edit_email.error = null
            return true
        }

    }

    private fun validatePassword() : Boolean {
        val passwrd: String = edit_pass .text.toString()
        val checkPasswrd = Regex("^" + "(?=.*[a-zA-Z])" + "(?=\\S+$)" + ".{4,10}" + "$")

        if (passwrd.isEmpty()) {
            edit_pass.error = "Field can not be empty"
            return false
        } else if (!passwrd.matches(checkPasswrd)) {
            edit_pass.error = "must contain at least 4 characters, at least 1 letter, no spaces"
            return false
        } else {
            edit_pass.error = null
            return true
        }

    }

    private fun privacy_policy_popup(){

        val builder = MaterialAlertDialogBuilder(
            this,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        )

        val tittle = SpannableString("Terms and Data Policy")
        tittle.setSpan(ForegroundColorSpan(Color.parseColor("#121212")), 0, tittle.length, 0)
        tittle.setSpan(StyleSpan(Typeface.BOLD), 0, tittle.length, 0)
        tittle.setSpan(TypefaceSpan("sans-serif-light"), 0, tittle.length, 0)

        builder.setTitle(tittle)
        builder.setMessage(Html.fromHtml("By Clicking continue, you agree to our <a href=\"https://www.twenty20app.com/terms\"> Terms of use </a>"+ "and"+ "<a href=\"https://www.twenty20app.com/privacy-center\"> Privacy Policy </a>"))
        builder.setPositiveButton("Continue", null)
        val dialogInterface = builder.create()
        dialogInterface.show()
        val mse = dialogInterface.findViewById<TextView>(android.R.id.message)
        mse!!.movementMethod = LinkMovementMethod.getInstance()
    }


}