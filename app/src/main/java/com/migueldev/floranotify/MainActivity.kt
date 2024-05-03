package com.migueldev.floranotify

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.migueldev.floranotify.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    //Um inteiro único por Activity
    private val REQU_ONE_TAP = 2
    private lateinit var callbackManager: CallbackManager
    private lateinit var buttonFacebookLogin: LoginButton
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
        val btGoogle = binding.btGoogle
        val btFacebook = binding.btFacebook

        buttonFacebookLogin = LoginButton(context)

        val user = mAuth.currentUser
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
                loginEmail()
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

        btGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.id_server))
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            val signInIntent =
                mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent,REQU_ONE_TAP)
        }


        btFacebook.setOnClickListener {
            FacebookSdk.sdkInitialize(context)
            //Inicialize botão Facebook
            callbackManager = CallbackManager.Factory.create()
            Log.d(TAG, "onCreate: teste")
            val loginManager = LoginManager
            loginManager.getInstance().registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        Log.d(TAG, "onSuccess: $result")
                        handleFacebookAccessToken(result.accessToken)
                    }

                    override fun onCancel() {
                        Log.d(TAG, "onCancel: login cancelado")
                    }

                    override fun onError(error: FacebookException) {
                        Log.d(TAG, "onError: $error")
                    }
                }
            )

        }
    }
    private fun loginEmail(){
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
    private fun loginGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken,null)

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){
                    task ->
                if (task.isSuccessful){
                    val intent = Intent(applicationContext, Principal::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext, R.string.verifique_a_sua_conex_o_e_tente_novamente, Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken){
        Log.d(TAG, "handleFacebookAccessToken: $token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Log.d(TAG, "handleFacebookAccessToken: success")
                    val intent = Intent(applicationContext, Principal::class.java)
                    startActivity(intent)
                }
                else {
                    Log.d(TAG, "handleFacebookAccessToken: ${task.exception}")

                    Toast.makeText(
                        applicationContext, R.string.verifique_a_sua_conex_o_e_tente_novamente, Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQU_ONE_TAP){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val conta = task.getResult(ApiException::class.java)
                loginGoogle(conta!!)
            } catch (e: ApiException){
                Log.d(TAG, "onActivityResult: $e")
                Log.d(TAG, "onActivityResult: ${e.status}")
            }
        }

        else {
            Log.d(TAG, "onActivityResult: O problema é o if")
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}