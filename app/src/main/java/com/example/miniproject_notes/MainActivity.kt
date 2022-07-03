package com.example.miniproject_notes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var recyclerView: RecyclerView
        lateinit var adapter_note : CustomAdapter
        lateinit var arrayList_note : ArrayList<DataClass>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrayList_note = ArrayList()

        fab_tambah.setOnClickListener {
            val intent= Intent(this@MainActivity, EditActivity::class.java)
            startActivity(intent)
        }

        loadData()
        buatAdapter()
        buatRecyclerView()
        saveData()
        noteClicked()

        search_field.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                searchField(p0.toString())
            }
        })
    }

    private fun searchField(text: String){
        val filteredList = ArrayList<DataClass>()

        for(item in arrayList_note){
            if(item.title.lowercase().contains(text.lowercase())){
                filteredList.add(item)
            }
        }

        adapter_note.filteredData(filteredList)
    }

    private fun loadData() {
        val save = getSharedPreferences("shareData", Context.MODE_PRIVATE)
        val gson = Gson()
        val empty = gson.toJson(arrayList_note)
        val json = save.getString("json", empty)

        val type = object : TypeToken<ArrayList<DataClass>>() {}.type
        arrayList_note = gson.fromJson(json, type)

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
    }


    private fun buatRecyclerView(){
        recyclerView = recycler_view
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter_note

    }

    private fun buatAdapter(){
        adapter_note = CustomAdapter(arrayList_note)
    }

    private fun noteClicked(){
        adapter_note.setItemClick(object: CustomAdapter.itemClick {
            override fun click(position: Int){
                Intent(this@MainActivity, EditActivity::class.java).also {
                    it.putExtra("POSITION", position)
                    startActivity(it)
                }
            }
        })
    }
}