package com.migueldev.floranotify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.migueldev.floranotify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        val view = binding.root
        val context = view.context
        val btCadastro = binding.btCadastro
        val btRedefinirSenha = binding.btRedefinir
        val btLogin = binding.btLogin
        setContentView(view)

        btLogin.setOnClickListener {
            
        }

        btCadastro.setOnClickListener {
            val intent = Intent(context, Cadastro::class.java)
            startActivity(intent)
        }
        btRedefinirSenha.setOnClickListener {
            val intent = Intent(context, RedefinirSenha::class.java)
            startActivity(intent)
        }

    }
}