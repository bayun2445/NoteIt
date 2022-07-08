package com.example.miniproject_notes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var recyclerView: RecyclerView
        lateinit var adapter_note : CustomAdapter
        lateinit var arrayList_note : ArrayList<DataClass>
        lateinit var tempArrayList : ArrayList<DataClass>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrayList_note = ArrayList()
        tempArrayList = ArrayList()


        search_field.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())

                if (searchText.isNotEmpty()){
                    arrayList_note.forEach{
                        if (it.title.lowercase(Locale.getDefault()).contains(searchText)){
                            tempArrayList.add(it)
                        }
                    }
                } else{
                    tempArrayList.clear()
                    tempArrayList.addAll(arrayList_note)
                    adapter_note.notifyDataSetChanged()
                }

                adapter_note.notifyDataSetChanged()
                return false
            }

        })

        fab_tambah.setOnClickListener {
            val intent= Intent(this@MainActivity, EditActivity::class.java)
            startActivity(intent)
        }

        loadData()
        buatAdapter()
        buatRecyclerView()
        saveData()
        noteClicked()

    }

    override fun onBackPressed() {

        val alertBackBuilder = AlertDialog.Builder(this)

        alertBackBuilder.setTitle("Keluar")
            .setMessage(" Anda yakin ingin keluar?")
            .setPositiveButton("Ya"){_,_->
                finish()
            }
            .setNegativeButton("Tidak"){dialog,_->
                dialog.cancel()
            }

        val alertBack = alertBackBuilder.create()
        alertBack.show()

    }


    private fun loadData() {
        val save = getSharedPreferences("shareData", Context.MODE_PRIVATE)
        val gson = Gson()
        val empty = gson.toJson(arrayList_note)
        val json = save.getString("json", empty)

        val type = object : TypeToken<ArrayList<DataClass>>() {}.type
        arrayList_note = gson.fromJson(json, type)
        tempArrayList.clear()
        tempArrayList.addAll(arrayList_note)

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


    private fun buatRecyclerView(){
        recyclerView = recycler_view
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter_note

    }

    private fun buatAdapter() {
        adapter_note = CustomAdapter(tempArrayList)
    }

    private fun noteClicked(){
        adapter_note.setItemClick(object: CustomAdapter.ItemClick {
            override fun click(position: Int){
                Intent(this@MainActivity, EditActivity::class.java).also {
                    it.putExtra("POSITION", position)
                    startActivity(it)
                }
            }
        })
    }
}