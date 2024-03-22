package com.migueldev.floranotify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.migueldev.floranotify.databinding.ActivityCadastroBinding

class Cadastro: AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        val view = binding.root
        val context = view.context
        setContentView(view)
    }
}