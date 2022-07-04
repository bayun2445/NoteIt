package com.example.miniproject_notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.template_note.view.*
import kotlin.collections.ArrayList

class CustomAdapter(private var arrayNote: ArrayList<DataClass>): RecyclerView.Adapter<CustomAdapter.CustomViewHolder> (){
    private lateinit var clicked: ItemClick

    interface ItemClick{
        fun click(position:Int)
    }

    fun setItemClick(isClicked: ItemClick){
        clicked = isClicked
    }

    class CustomViewHolder(parent: View, itemClick: ItemClick): RecyclerView.ViewHolder (parent){
        var title: TextView?= itemView.text_judul
        var deskripsi: TextView?= itemView.text_deskripsi

        init{
            itemView.setOnClickListener {
                itemClick.click(absoluteAdapterPosition)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.template_note, parent, false)

        return CustomViewHolder(inflater, clicked)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.title?.text = arrayNote[position].title
        holder.deskripsi?.text = arrayNote[position].deskripsi
    }

    override fun getItemCount(): Int {
        return arrayNote.size
    }

}