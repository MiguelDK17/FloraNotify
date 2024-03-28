package com.migueldev.floranotify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.migueldev.floranotify.databinding.RedefinirSenhaBinding

class RedefinirSenha: AppCompatActivity() {
    private lateinit var binding: RedefinirSenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RedefinirSenhaBinding.inflate(layoutInflater)
        val view = binding.root
        val context = view.context
        setContentView(view)

    }
}