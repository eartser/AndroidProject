package com.elizavetaartser.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.listOf as listOf

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)

        val adapter = UserAdapter()

        recyclerView.adapter = adapter
        adapter.userList = loadUsers()
        adapter.notifyDataSetChanged()
    }

    private fun loadUsers(): List<User> {
        return listOf(
            User(
                avatarUrl = "",
                userName = "User name 1",
                groupName = "A"
            ),
            User(
                avatarUrl = "",
                userName = "User name 2",
                groupName = "A"
            ),
            User(
                avatarUrl = "",
                userName = "User name 1",
                groupName = "A"
            ),
            User(
                avatarUrl = "",
                userName = "Use",
                groupName = "A"
            ),
            User(
                avatarUrl = "",
                userName = "User name 3",
                groupName = "B"
            ),
            User(
                avatarUrl = "",
                userName = "User name 4",
                groupName = "B"
            ),
            User(
                avatarUrl = "",
                userName = "User name 5",
                groupName = "A"
            ),
            User(
                avatarUrl = "",
                userName = "User name 6",
                groupName = "A"
            ),
            User(
                avatarUrl = "",
                userName = "User name 7",
                groupName = "B"
            ),
            User(
                avatarUrl = "",
                userName = "User name 8",
                groupName = "A"
            ),
        )
    }
}