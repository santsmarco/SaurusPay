package com.app.sauruspay.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.sauruspay.databinding.ActivityLoginBinding
import com.app.sauruspay.model.LoginRequest
import com.app.sauruspay.model.LoginResponse
import com.app.sauruspay.network.RetrofitClient
import com.app.sauruspay.utils.Navigation
import com.app.sauruspay.utils.Preferences
import com.app.sauruspay.utils.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val usuario = binding.edtUsuario.text.toString()
            val senha = binding.edtSenha.text.toString()
            if (usuario.isNotEmpty() && senha.isNotEmpty()) {
                performLogin("061f92f5-f2a2-410a-8e2b-b3a28132c258",usuario, senha)
            } else { Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun performLogin(applicationId: String,usuario: String, senha: String) {
        if (!Util.verifyWifi(this)) {
            Util.showDialog(this, "Wi-Fi não habilitado", "Por favor, habilite o Wi-Fi para continuar.", "OK")
            return
        }
        val loginRequest = LoginRequest(applicationId, usuario, senha)
        val apiService = RetrofitClient.getInstance(this)
        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if(response.body() != null){
                        val username = response.body()!!.credenciais[0].username
                        val aplicacaoId = response.body()!!.credenciais[0].aplicacaoid
                        Preferences.saveUserCredentials(username, aplicacaoId, this@LoginActivity)
                        Toast.makeText(this@LoginActivity, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        Navigation.startActivity(OrderActivity::class.java, this@LoginActivity)
                    }
                } else { Util.showDialog(this@LoginActivity, "Falha no login", "Houve uma falha no login!\nCode: ${response.code()} \nMessage: ${response.errorBody()?.string() ?: "Not found"}", "OK") }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}