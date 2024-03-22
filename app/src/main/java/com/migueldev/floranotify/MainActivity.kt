package com.migueldev.floranotify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.migueldev.floranotify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        val context = view.context
        val btCadastro = binding.btCadastro
        setContentView(view)

        btCadastro.setOnClickListener {
            val intent = Intent(context, Cadastro::class.java)
            startActivity(intent)
        }

    }
}