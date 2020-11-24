package com.example.githubuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView
    private var list: ArrayList<User> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.setHasFixedSize(true)

        list.addAll(UserData.listData)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        recyclerview.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        recyclerview.adapter = listUserAdapter
    }

}