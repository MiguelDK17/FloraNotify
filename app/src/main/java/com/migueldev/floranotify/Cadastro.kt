package com.migueldev.floranotify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.migueldev.floranotify.databinding.ActivityCadastroBinding


class Cadastro: AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        val view = binding.root
        val context = view.context
        setContentView(view)

        val edtSenha = binding.edtSenha.text
        val edtRepitaSenha = binding.edtRepitaSenha.text
        val btCriarConta = binding.btCriarConta

        btCriarConta.setOnClickListener {
            if (edtSenha.toString() == edtRepitaSenha.toString()){
                criaConta()
            }
            else {
                Toast.makeText(
                    context,"As senhas fornecidas não estão iguais"
                    ,Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
    private fun criaConta(){
        val view = binding.root
        val nome = binding.edtNome.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtSenha.text.toString()
        mAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    //Se a criação da conta for um sucesso
                    val uid = mAuth.currentUser?.uid
                    val dados = hashMapOf(
                        "id" to uid.toString(),
                        "nome" to nome,
                        "foto" to "foto"
                    )
                    val profilerUpdates = userProfileChangeRequest {
                        displayName = nome
                    }

                    val user = mAuth.currentUser
                    user!!.updateProfile(profilerUpdates)
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                db.collection("Users")
                                    .document(uid.toString())
                                    .set(dados)
                                    .addOnSuccessListener { document ->
                                        val intent = Intent(this, Principal::class.java)
                                        startActivity(intent)
                                    }
                            }
                        }

                }
            }
            .addOnFailureListener { error->
                Toast.makeText(
                    this,getString(R.string.verifique_conexao_internet)
                    ,Toast.LENGTH_SHORT).show()
            }

    }
}