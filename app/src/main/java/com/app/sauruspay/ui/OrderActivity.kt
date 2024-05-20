package com.app.sauruspay.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sauruspay.adapter.FaturaAdapter
import com.app.sauruspay.databinding.ActivityOrderBinding
import com.app.sauruspay.model.FaturaResponse
import com.app.sauruspay.network.RetrofitClient
import com.app.sauruspay.utils.Navigation
import com.app.sauruspay.utils.Preferences
import com.app.sauruspay.utils.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity(), FaturaAdapter.OnItemClickListener {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var faturaAdapter: FaturaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerFatura.layoutManager = LinearLayoutManager(this)
        faturaAdapter = FaturaAdapter()
        faturaAdapter.setListener(this)
        binding.recyclerFatura.adapter = faturaAdapter
        binding.txtUser.text = Preferences.recupereUser(this)
        recupereFaturas()

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                faturaAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnLogout.setOnClickListener {
            Preferences.logout(this)
        }
    }

    private fun recupereFaturas(){
        val requestBody = Preferences.recupereCredentials(this)
        if (requestBody != null) {
            val apiService = RetrofitClient.getInstance(this)
            apiService.getInvoices(requestBody.aplicacaoId, requestBody.username).enqueue(object : Callback<FaturaResponse> {
                override fun onResponse(call: Call<FaturaResponse>, response: Response<FaturaResponse>) {
                    if (response.isSuccessful) {
                        val faturaResponse = response.body()
                        faturaResponse?.let {
                            faturaAdapter.submitList(it.list)
                        }
                    } else {
                        Util.showDialogFunction(this@OrderActivity, "Falha ao recuperar faturas", "Erro: ${response.errorBody().toString()}", "TENTAR NOVAMENTE") {
                            recupereFaturas()
                        }
                    }
                }

                    override fun onFailure(call: Call<FaturaResponse>, t: Throwable) {
                        Util.showDialogFunction(this@OrderActivity, "Falha na requisição", "Erro: ${t.message}","TENTAR NOVAMENTE") { recupereFaturas() }
                    }
                })
        }else{ Util.showDialog(this@OrderActivity,"Falha nas credenciais", "Não foi possível buscar as credenciais salvas para efetuar a requisição, faça login novamente!", "OK") }
    }

    override fun onItemClick(position: Int) {
        val clickedFaturaItem = faturaAdapter.faturaItems[position]
        Navigation.startActivityPayment(PaymentActivity::class.java, this@OrderActivity, clickedFaturaItem)
    }
}