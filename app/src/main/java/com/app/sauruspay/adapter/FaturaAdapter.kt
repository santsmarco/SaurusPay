package com.app.sauruspay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sauruspay.R
import com.app.sauruspay.model.FaturaItem

class FaturaAdapter : RecyclerView.Adapter<FaturaAdapter.FaturaViewHolder>() {

    var faturaItems: List<FaturaItem> = listOf()
    private var faturaItemsFull: MutableList<FaturaItem> = mutableListOf()
    private var listener: OnItemClickListener? = null

    inner class FaturaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                listener?.onItemClick(position)
            }
        }
        private val numeroFaturaTextView: TextView = itemView.findViewById(R.id.numeroFaturaTextView)
        private val historicoTextView: TextView = itemView.findViewById(R.id.historicoTextView)
        private val valorFaturaTextView: TextView = itemView.findViewById(R.id.valorFaturaTextView)
        private val nome: TextView = itemView.findViewById(R.id.txtName)
        private val cpfCnpj: TextView = itemView.findViewById(R.id.txtCpfCnpj)

        fun bind(faturaItem: FaturaItem) {
            numeroFaturaTextView.text = "• Número: ${faturaItem.numeroFatura}"
            historicoTextView.text = "• Hist.: ${faturaItem.historico}"
            valorFaturaTextView.text = "• Valor: ${faturaItem.valorFatura.toString()}"
            nome.text = "• Cliente: ${faturaItem.pessoa.nome}"
            cpfCnpj.text = "• Cpf/Cnpj.: ${faturaItem.pessoa.cpfCnpj}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaturaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fatura, parent, false)
        return FaturaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaturaViewHolder, position: Int) {
        val faturaItem = faturaItems[position]
        holder.bind(faturaItem)
    }

    override fun getItemCount(): Int {
        return faturaItems.size
    }

    fun submitList(faturaList: List<FaturaItem>) {
        this.faturaItems = faturaList
        this.faturaItemsFull = ArrayList(faturaList)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val filteredList = faturaItemsFull.filter {
            it.pessoa.nome.contains(query, true) || it.pessoa.cpfCnpj.contains(query, true)
        }
        faturaItems = ArrayList(filteredList)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
