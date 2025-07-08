package com.calberto_barbosa_jr.interactus.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.calberto_barbosa_jr.interactus.R
import com.calberto_barbosa_jr.interactus.databinding.ActivityAuthenticationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText


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
        // Ligando os componentes do layout aos objetos no código
        emailInput = findViewById(R.id.editEmailLogin)
        passwordInput = findViewById(R.id.editPasswordLogin)
    }

    // Método acionado pelo botão "Login"
    fun login(view: View) {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()

        // Validação de entrada
        if (email.isEmpty() || password.isEmpty()) {
            showErrorMessage("Por favor, preencha todos os campos.")
            return
        }

        // Realizando login com o Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido
                    Log.d("AuthenticationActivity", "signInWithEmail:success")
                    val user = auth
                    updateUI(user)
                    Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    // Falha no login: tratar erros
                    handleLoginError(task.exception)
                }
            }
    }

    // Método para tratar possíveis erros de login
    private fun handleLoginError(exception: Exception?) {
        val errorMessage = when (exception) {
            is FirebaseAuthInvalidUserException -> "Usuário não encontrado. Verifique o e-mail e tente novamente."
            is FirebaseAuthInvalidCredentialsException -> "Senha incorreta. Por favor, tente novamente."
            is FirebaseNetworkException -> "Sem conexão com a internet."
            else -> "Erro ao realizar login. Tente novamente mais tarde."
        }
        showErrorMessage(errorMessage)
        Log.w("AuthenticationActivity", "signInWithEmail:failure", exception)
    }

    // Exibe mensagens de erro na interface
    private fun showErrorMessage(message: String) {
        val errorMessageTextView = findViewById<TextView>(R.id.errorMessageLogin)
        errorMessageTextView.text = message
    }

    // Atualiza a interface do usuário após o login
    private fun updateUI(user: FirebaseAuth?) {
        if (user != null) {
            //val openActivity = OpenActivity()
            //openActivity.login(this)
        } else {
            showErrorMessage("Erro ao autenticar o usuário.")
        }
    }

    fun signup(view: View) {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    fun recover(view: View) {
        val email: String = binding.editEmailLogin.text.toString()

        if (email.isEmpty()) {
            binding.errorMessageLogin.text = "Enter your email \nfor the new password \nto be sent"
            //showToast("Enter your email for the new password to be sent")
        } else {
            sendNewPassword(email)
        }
    }

    private fun sendNewPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            binding.errorMessageLogin.text = "We sent an email with a link \nto reset your password"
            //showToast("We sent an email with a link to reset your password")
        }.addOnFailureListener {
            binding.errorMessageLogin.text = "Enter valid email"
            //showError("Enter valid email")
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            //OpenActivity().login(this)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }


}