package com.app.sauruspay.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.app.sauruspay.databinding.ActivityPaymentBinding
import com.app.sauruspay.model.FaturaItem
import com.app.sauruspay.model.InvoiceFinalizePayment
import com.app.sauruspay.model.ResponsePayment
import com.app.sauruspay.network.RetrofitClient
import com.app.sauruspay.utils.MaskEditUtil
import com.app.sauruspay.utils.Preferences
import com.app.sauruspay.utils.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var invoice: FaturaItem? = null
    private var selectedInstallment by Delegates.notNull<Int>()
    private var typePosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialConfigs()

        binding.spinnerPaymentType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typePosition = position
                visibility(typePosition)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnPay.setOnClickListener {
            pay()
        }
    }

    fun pay(){
        if (!Util.verifyWifi(this)) {
            Util.showDialog(this, "Wi-Fi não habilitado", "Por favor, habilite o Wi-Fi para continuar.", "OK")
            return
        }
        if (!validateInputs()) {
            Util.showDialog(this, "Campos inválidos", "Por favor, verifique os dados inseridos.", "OK")
            return
        }
        val requestBody = Preferences.recupereCredentials(this)
        invoice?.let {
            val data = InvoiceFinalizePayment(it.numeroFatura, it.historico, it.valorFatura.toInt(), it.pagamentoParcial, it.pessoa, it.valorFatura.toInt(), "idTransacao", "nsu", "codAut", "codControle", "dRetorno", "numCartao", "bandeira", "rede", "adquirente", 0, 1, selectedInstallment, "dTransacao", 0, "msgRetorno", "arqRetorno")

            if (requestBody != null) {
                val apiService = RetrofitClient.getInstance(this)
                apiService.sendTransactionData(requestBody.aplicacaoId, requestBody.username, data).enqueue(object : Callback<ResponsePayment> {
                    override fun onResponse(call: Call<ResponsePayment>, response: Response<ResponsePayment>) {
                        if (response.isSuccessful) {
                            val responsePayment = response.body()
                            if(responsePayment!!.id.isNotEmpty()){
                                Util.showDialogFunction(this@PaymentActivity, "PAGAMENTO EFETUADO COM SUCESSO", "", "OK") { finish() }
                            }
                        } else {
                            Util.showDialog(this@PaymentActivity, "Pagamento Error", "Erro: ${response.errorBody().toString()}", "OK")
                        }
                    }

                    override fun onFailure(call: Call<ResponsePayment>, t: Throwable) {
                        Util.showDialog(this@PaymentActivity, "Falha na requisição", "Erro: ${t.message}", "OK")
                    }
                })
            }else{
                Util.showDialog(this@PaymentActivity,"Falha nas credenciais", "Não foi possível buscar as credenciais salvas para efetuar a requisição, faça login novamente!", "OK")
            }
        }
    }

    fun visibility(position: Int){
        if (position == 0) {
            binding.spinnerInstallments.isEnabled = true
            binding.ctrlCard.visibility = View.VISIBLE
            binding.ctrlPix.visibility = View.GONE
        } else if (position == 1){
            binding.spinnerInstallments.isEnabled = false
            binding.spinnerInstallments.setSelection(0)
            binding.ctrlCard.visibility = View.VISIBLE
            binding.ctrlPix.visibility = View.GONE
        } else if (position == 2){
            binding.spinnerInstallments.isEnabled = false
            binding.spinnerInstallments.setSelection(0)
            binding.ctrlCard.visibility = View.GONE
            binding.ctrlPix.visibility = View.VISIBLE
            clearEdts()
        }
    }

    fun initialConfigs(){
        invoice = intent.getSerializableExtra("faturaItem") as FaturaItem?
        binding.tvNumeroFatura.text = "• Número da Fatura: ${invoice?.numeroFatura}"
        binding.tvHistorico.text = "• Histórico: ${invoice?.historico}"
        binding.tvValorFatura.text = "• Valor da Fatura: ${invoice?.valorFatura}"
        binding.tvPessoa.text = "• Cliente: ${invoice?.pessoa!!.nome} - CPF: ${invoice?.pessoa!!.cpfCnpj} - Cód: ${invoice?.pessoa!!.codigo}"
        binding.tvPagamentoParcial.text = "• Pagamento Parcial: ${if(invoice?.pagamentoParcial == true) "Sim" else "Não"}"
        binding.edtNumberCard.addTextChangedListener(MaskEditUtil.mask(binding.edtNumberCard, MaskEditUtil.FORMAT_CARD))
        binding.edtDateValid.addTextChangedListener(MaskEditUtil.mask(binding.edtDateValid, MaskEditUtil.FORMAT_DATE))
        binding.edtSecurityCode.addTextChangedListener(MaskEditUtil.mask(binding.edtSecurityCode, MaskEditUtil.FORMAT_CV))

        val paymentMethods = arrayOf("CREDITO", "DEBITO", "PIX")
        val paymentMethodAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        binding.spinnerPaymentType.adapter = paymentMethodAdapter

        val instalments = (1..12).map { it.toString() }.toTypedArray()
        val instalmentsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, instalments)
        binding.spinnerInstallments.adapter = instalmentsAdapter

        binding.spinnerInstallments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedInstallment = instalments[position].toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun validateInputs(): Boolean {
        val numberCard = binding.edtNumberCard.text.toString()
        val dateValid = binding.edtDateValid.text.toString()
        val securityCode = binding.edtSecurityCode.text.toString()
        val pix = binding.edtPix.text.toString()
        if(typePosition == 0 || typePosition == 1){
            if (numberCard.length != 19) {
                binding.edtNumberCard.error = "Número de cartão inválido"
                return false
            }
            if (!dateValid.matches(Regex("\\d{2}/\\d{2}"))) {
                binding.edtDateValid.error = "Data de validade inválida"
                return false
            }
            if (securityCode.length != 3 && securityCode.length != 4) {
                binding.edtSecurityCode.error = "Código de segurança inválido"
                return false
            }
        }else {
            if (pix.isNullOrEmpty()) {
                binding.edtPix.error = "Pix não digitado"
                return false
            }
        }

        return true
    }

    fun clearEdts(){
        binding.edtNumberCard.setText("")
        binding.edtDateValid.setText("")
        binding.edtSecurityCode.setText("")
        binding.edtPix.setText("")
    }
}