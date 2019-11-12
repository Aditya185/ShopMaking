package com.adityaprakash.shopmaking

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*
import java.security.AccessController.getContext

class QrCodeAdapter(private val qrList: ArrayList<QrCode>) : RecyclerView.Adapter<QrCodeAdapter.QrHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QrHolder {
        return QrHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
    }

    override fun getItemCount() = qrList.size

    override fun onBindViewHolder(holder: QrHolder, position: Int) {
        with(qrList[position]) {
            holder.itemView.qrType.text = this.type
            holder.itemView.qrValue.text = this.value
            val context = holder.itemView.context

            holder.itemView.mCardView.setOnClickListener{
                val intent = Intent(context, BarCodeSearchActivity::class.java)
                intent.putExtra("key",this.value)
                context.startActivity(intent)

            }


        }


    }

    class QrHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}