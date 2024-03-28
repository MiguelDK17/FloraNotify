package com.migueldev.floranotify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.migueldev.floranotify.databinding.ActivityPrincipalBinding

class Principal: AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        val view = binding.root
        val context = view.context
        setContentView(view)
    }
}