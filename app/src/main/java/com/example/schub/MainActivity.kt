package com.example.schub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val websites = listOf(
        Website("Heise", "https://www.heise.de"),
        Website("Tagesschau", "https://www.tagesschau.de"),
        Website("Wikipedia", "https://www.wikipedia.org")
        // hier kannst du beliebig weitere hinzufügen
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // 3 Kacheln pro Reihe
        recyclerView.adapter = WebsiteAdapter(websites) { website ->
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", website.url)
            intent.putExtra("title", website.title)
            startActivity(intent)
        }
    }
}
