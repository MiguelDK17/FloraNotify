package com.migueldev.floranotify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.migueldev.floranotify.databinding.ActivityPrincipalBinding

class Principal: AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val floatingActionButton = binding.floatingActionButton
        if (FirebaseAuth.getInstance().currentUser == null){
            println(null)
        }
        else {
            println(FirebaseAuth.getInstance().currentUser)
        }


        floatingActionButton.setOnClickListener {
            //Criada a variável e instancia do menu criado para o botão de ação flutuante
            val popupMenu = PopupMenu(binding.root.context, floatingActionButton)
            popupMenu.menuInflater.inflate(R.menu.options_menu, popupMenu.menu)
            //ação de clique do menu
            popupMenu.setOnMenuItemClickListener { menuItem ->
                //se a opção menu de perfil for clicada, o usuário será mandado para a tela perfil
                when (menuItem.itemId) {
                    //Se a opção logout for clicada o usuário fará logout no app
                    R.id.menu_logout -> {
                        alertDialog()
                        true
                    }
                    //Se qualquer opção além dessas for clicada o menu irá se fechar
                    else -> false
                }
            }
            popupMenu.show()
        }

    }
    //Dialog que alerta o usuário ao fazer logoff
  private fun alertDialog() {
        //Instâcia do AlertDialog
        val builder = binding.root.context.let { AlertDialog.Builder(it) }
        builder.apply {
            setTitle(
                "Você será desconectado do app"
            )
            setMessage(
                "Deseja continuar ?"
            )
            setPositiveButton(
                "Sim"
            ) { dialog, which ->
                Firebase.auth.signOut()
                val logout = Intent(binding.root.context, MainActivity::class.java)
                startActivity(logout)
            }
            setNegativeButton(
                "Não"
            ) { dialog, which ->
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}