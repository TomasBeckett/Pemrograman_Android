package com.example.m4

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MahasiswaAdapter(private val listMahasiswa: List<Mahasiswa>) :
    RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.itemNama)
        val tvNim: TextView = itemView.findViewById(R.id.itemNIM)
        val tvProdi: TextView = itemView.findViewById(R.id.itemProdi)
        val imgFoto: ImageView = itemView.findViewById(R.id.itemFoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mahasiswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listMahasiswa[position]
        holder.tvNama.text = item.nama
        holder.tvNim.text = item.nim
        holder.tvProdi.text = item.prodi

        // Implementasi Glide
        Glide.with(holder.itemView.context)
            .load(item.foto)
            .placeholder(R.drawable.outline_broken_image_24)
            .error(R.drawable.outline_broken_image_24)
            .into(holder.imgFoto)
    }

    override fun getItemCount(): Int {
        return listMahasiswa.size
    }
}
