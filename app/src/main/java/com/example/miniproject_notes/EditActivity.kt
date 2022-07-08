package com.example.miniproject_notes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.miniproject_notes.MainActivity.Companion.adapter_note
import com.example.miniproject_notes.MainActivity.Companion.arrayList_note
import com.example.miniproject_notes.MainActivity.Companion.tempArrayList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.edit_note.*

class EditActivity: AppCompatActivity() {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        val pos = intent.getIntExtra("POSITION", -1)
        var tempTitle = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_note)

        if (pos != -1){
            edit_title.setText(tempArrayList[pos].title)
            edit_deskripsi.setText(tempArrayList[pos].deskripsi)
            tempTitle = tempArrayList[pos].title
        }
        else{
            btn_delete.visibility = View.GONE
        }

        btn_save.setOnClickListener {

            val title = edit_title .text.toString()
            val deskripsi = edit_deskripsi.text.toString()

            if (title.isEmpty() && deskripsi.isEmpty()){
                val builder = AlertDialog.Builder(this)

                builder.setMessage("Tidak dapat menyimpan draf kosong!")

                val alertEmpty = builder.create()
                alertEmpty.show()
            }
            else{
                var index = -1

                if (pos != -1){
                    var count = -1
                    arrayList_note.forEach{
                        count++
                        if (it.title.lowercase()== tempArrayList[pos].title.lowercase()){
                            index = count

                        }
                    }
                    arrayList_note[index] = DataClass(title, deskripsi)
                    tempArrayList[pos] = DataClass(title, deskripsi)
                    adapter_note.notifyItemChanged(pos)
                }
                else {
                    val position = arrayList_note.size
                    arrayList_note.add(DataClass(title, deskripsi))
                    adapter_note.notifyItemInserted(position)
                }

                saveData()
                finish()
            }

        }

        btnback.setOnClickListener {

            backDialog()
        }

        btn_delete.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Hapus")
                .setMessage("Apakah Anda yakin untuk menghapus catatan?")
                .setPositiveButton("Ya") { _, _ ->
                    var index = -1
                    for (row in arrayList_note){
                        index++
                        if (row.title == tempTitle){
                            break
                        }
                    }
                    arrayList_note.removeAt(index)

                    saveData()
                    adapter_note.notifyDataSetChanged()
                    finish()
                }
                .setNegativeButton("Tidak"){ dialog,_ ->
                    dialog.cancel()
                }

            val alertDelete = builder.create()
            alertDelete.show()
        }


    }

    override fun onBackPressed() {
        backDialog()
    }

    private fun backDialog(){
        val pos = intent.getIntExtra("POSITION", -1)
        val title = edit_title .text.toString()
        val deskripsi = edit_deskripsi.text.toString()

        if (pos == -1){
            if(edit_title.text.isEmpty() && edit_deskripsi.text.isEmpty()){
                finish()
            }
            else{
                val alertBackBuilder = AlertDialog.Builder(this)

                alertBackBuilder.setTitle("Simpan Draf")
                    .setMessage("Apakah Anda ingin menyimpan sebelum menutup?")
                    .setPositiveButton("Ya"){_,_->
                        val position = arrayList_note.size
                        arrayList_note.add(DataClass(title, deskripsi))
                        adapter_note.notifyItemInserted(position)

                        saveData()
                        finish()
                    }
                    .setNegativeButton("Tidak"){_,_->
                        finish()
                    }

                val alertBack = alertBackBuilder.create()
                alertBack.show()
            }
        }
        else if (title != tempArrayList[pos].title ||deskripsi != tempArrayList[pos].deskripsi ){
            val alertBackBuilder = AlertDialog.Builder(this)
            var index = -1
            var count = -1
            alertBackBuilder.setTitle("Simpan Perubahan")
                .setMessage("Apakah Anda ingin menyimpan perubahan sebelum menutup?")
                .setPositiveButton("Ya"){_,_->

                    arrayList_note.forEach{
                        count++
                        if (it.title.lowercase()== tempArrayList[pos].title.lowercase()){
                            index = count

                        }
                    }
                    arrayList_note[index] = DataClass(title, deskripsi)
                    tempArrayList[pos] = DataClass(title, deskripsi)
                    adapter_note.notifyItemChanged(pos)

                    saveData()
                    finish()
                }
                .setNegativeButton("Tidak"){_,_->
                    finish()
                }

            val alertBack = alertBackBuilder.create()
            alertBack.show()

        }
        else {
            finish()
        }

    }


    private fun saveData() {
        val save = getSharedPreferences("shareData", Context.MODE_PRIVATE)
        val editor = save.edit()
        val gson = Gson()
        val json = gson.toJson(arrayList_note)



        editor.apply {
            putString("json", json)
            apply()
        }

        tempArrayList.clear()
        tempArrayList.addAll(arrayList_note)

    }
}