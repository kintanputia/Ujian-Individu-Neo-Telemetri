package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth= FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val email=editEmail.text.toString().trim()
            val password =editPassword.text.toString().trim()

            if (email.isEmpty()){
                editEmail.error="Email harus diisi"
                editEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editEmail.error="Email tidak valid"
                editEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()||password.length<6){
                editPassword.error="Password harus lebih dari 6 karakter"
                editPassword.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password)
        }

        btnHaveAccount.setOnClickListener{
            Intent(this@RegisterActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun registerUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Intent(this@RegisterActivity, HomeActivity::class.java).also{
                            it.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT ).show()
                    }
                }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null){
            Intent(this@RegisterActivity, HomeActivity::class.java).also{intent->
                intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}