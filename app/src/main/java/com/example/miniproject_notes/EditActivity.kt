package com.example.miniproject_notes

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.miniproject_notes.MainActivity.Companion.adapter_note
import com.example.miniproject_notes.MainActivity.Companion.arrayList_note
import com.example.miniproject_notes.MainActivity.Companion.tempArrayList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.edit_note.*

class EditActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_note)
        val pos = intent.getIntExtra("POSITION", -1)

        if (pos != -1){
            edit_title.setText(tempArrayList[pos].title)
            edit_deskripsi.setText(tempArrayList[pos].deskripsi)
        }

        btn_save.setOnClickListener {
            val title = edit_title .text.toString()
            val deskripsi = edit_deskripsi.text.toString()
            var index = -1

            if (pos != -1){

                var count = -1
                for(row in arrayList_note){
                    count++
                    if (row.title.lowercase()== tempArrayList[pos].title.lowercase()){
                        index = count
                        break
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

        btnback.setOnClickListener {
            finish()
        }

        btn_delete.setOnClickListener{
            arrayList_note.removeAt(pos)
            adapter_note.notifyItemRemoved(pos)
            saveData()
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