package com.calberto_barbosa_jr.interactus.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit
import com.calberto_barbosa_jr.interactus.R
import com.calberto_barbosa_jr.interactus.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth

        applyWindowInsets()
        setupVerificationCallbacks()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupVerificationCallbacks() {
        verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                logAndDisplayMessage("Verification completed: $credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                logAndDisplayMessage("Verification failed", exception)

                val errorMessage = when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid phone number."
                    is FirebaseTooManyRequestsException -> "Too many requests. Try again later."
                    else -> "Unknown error: ${exception.localizedMessage}"
                }

                logAndDisplayMessage(errorMessage)
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@AuthenticationActivity.verificationId = verificationId
                resendToken = token

                logAndDisplayMessage("Verification code sent. Please enter the code.")
            }
        }
    }

    fun buttonPhoneNumber(view: View) {
        val phoneNumber = binding.editTextPhoneNumber.text.toString().trim()

        if (phoneNumber.isEmpty()) {
            logAndDisplayMessage("Phone number cannot be empty.")
            return
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(verificationCallbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun buttonVerificationCode(view: View) {
        val code = binding.editTextVerificationCode.text.toString().trim()

        if (code.isEmpty()) {
            logAndDisplayMessage("Verification code cannot be empty.")
            return
        }

        verificationId?.let {
            val credential = PhoneAuthProvider.getCredential(it, code)
            signInWithPhoneAuthCredential(credential)
        } ?: logAndDisplayMessage("Error: Verification ID not found. Please request a new code.")
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logAndDisplayMessage("Sign-in successful.")
                    navigateToHomeScreen()
                } else {
                    logAndDisplayMessage("Sign-in failed", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        logAndDisplayMessage("Invalid verification code.")
                    }
                }
            }
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            navigateToHomeScreen()
        }
    }

    private fun logAndDisplayMessage(message: String, exception: Exception? = null) {
        binding.mensagemText.text = message
        exception?.let { Log.e("Auth", message, it) } ?: Log.d("Auth", message)
    }
}