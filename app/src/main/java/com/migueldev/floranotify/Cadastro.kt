package com.migueldev.floranotify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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
    }
    private fun criaConta(){
        val nome = binding.edtNome.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtSenha.text.toString()
        mAuth = FirebaseAuth.getInstance()
        val db = Firebase.

    }
}