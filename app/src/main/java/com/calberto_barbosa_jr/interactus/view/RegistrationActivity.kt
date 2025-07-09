package com.calberto_barbosa_jr.interactus.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.calberto_barbosa_jr.interactus.R
import com.calberto_barbosa_jr.interactus.databinding.ActivityRegistrationBinding
import com.calberto_barbosa_jr.interactus.model.UserProfile
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var repeatPasswordInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializando o Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Ligando os componentes do layout aos objetos no código
        nameInput = binding.editName
        emailInput = binding.editEmailRegister
        passwordInput = binding.editPasswordRegister
        repeatPasswordInput = binding.editRepeatPassword
    }

    fun registration(view: View) {
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()
        val repeatPassword = repeatPasswordInput.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            showErrorMessage("Todos os campos devem ser preenchidos.")
            return
        }

        if (password != repeatPassword) {
            showErrorMessage("As senhas não coincidem.")
            return
        }

        if (password.length < 6) {
            showErrorMessage("A senha deve ter pelo menos 6 caracteres.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        saveUserToFirestore(user.uid, name, email)
                        updateUI(user)
                        Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    handleRegistrationError(task.exception)
                }
            }
    }

    private fun saveUserToFirestore(uid: String, name: String, email: String) {
        val user =
            UserProfile(name = name, email = email)  // CPF pode ser adicionado depois, se quiser.
        firestore.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d("RegistrationActivity", "Usuário salvo no Firestore com sucesso!")
            }
            .addOnFailureListener { e ->
                Log.w("RegistrationActivity", "Erro ao salvar usuário no Firestore", e)
            }
    }

    private fun showErrorMessage(message: String) {
        binding.errorMessageRegister.text = message
    }

    private fun handleRegistrationError(exception: Exception?) {
        val message = when (exception) {
            is FirebaseAuthWeakPasswordException -> "A senha é muito fraca."
            is FirebaseAuthInvalidCredentialsException -> "E-mail inválido."
            is FirebaseAuthUserCollisionException -> "Este e-mail já está registrado."
            is FirebaseNetworkException -> "Sem conexão com a internet."
            else -> "Erro ao registrar o usuário. Tente novamente."
        }
        showErrorMessage(message)
        Log.w("RegistrationActivity", "createUserWithEmail:failure", exception)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "Bem-vindo, ${user.email}!", Toast.LENGTH_SHORT).show()
        } else {
            showErrorMessage("Erro ao autenticar o usuário.")
        }
    }
}