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
        setContentView(view)

        val btCadastro = binding.btCadastro
        val btRedefinirSenha = binding.btRedefinir
        val btLogin = binding.btLogin
        val edtEmail = binding.edtUsuario
        val edtSenha = binding.edtSenha

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(applicationContext, Principal::class.java)
            startActivity(intent)
        }

        btLogin.setOnClickListener {
            //Pega o conteúdo dos edits usuario e senha
            //se o conteúdo estiver vazio irá alertá-lo
            if ((edtEmail.text.toString()
                    .trim { it <= ' ' } == "") && (edtSenha.text.toString()
                    .trim { it <= ' ' } == "")
            ) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.os_campos_n_o_podem_ficar_vazios),
                    Toast.LENGTH_SHORT
                ).show()
                //se não, pega apenas o do usuário e o alerta se estiver vazio
            } else if ((edtEmail.text.toString().trim { it <= ' ' } == "")) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.o_campo_email_n_o_pode_ficar_vazio), Toast.LENGTH_SHORT
                ).show()
                //se não se apenas o campo senha estiver vazio o app irá alertá-lo
            } else if ((edtSenha.text.toString().trim { it <= ' ' } == "")) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.o_campo_senha_n_o_pode_ficar_vazio), Toast.LENGTH_SHORT
                ).show()
                //se não irá prosseguir para a função login
            } else {
                login()
            }
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
    private fun login(){
        val edtUsuario = binding.edtUsuario
        val edtSenha = binding.edtSenha
        val pbCarregando = binding.progressBar
        val mAuth = FirebaseAuth.getInstance()

        val email = edtUsuario.text.toString().trim { it <= ' ' }
        val password = edtSenha.text.toString().trim { it <= ' ' }
        pbCarregando.visibility = View.VISIBLE

        //Variável mAuth chama o método para fazer login com email e senha
        mAuth.signInWithEmailAndPassword(
            email,
            password
        ) //adiciona o método para completar a requisição
            .addOnSuccessListener(this) {
                val login = Intent(this@MainActivity, Principal::class.java)
                //Irá aparecer uma mensagem na tela avisando que o login foi completado e irá avnaçar para
                //a próxima tela
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.login_realizado_com_sucesso),
                    Toast.LENGTH_SHORT
                ).show()
                pbCarregando.visibility = View.GONE
                startActivity(login)
            }
            .addOnFailureListener {
                //se não irá avisar que o login deu errado
                pbCarregando.visibility = View.GONE
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.email_ou_senha_incorretos),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}