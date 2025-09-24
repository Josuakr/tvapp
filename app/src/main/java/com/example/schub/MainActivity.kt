package com.example.schub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val websites = listOf(
        Website("PStream", "https://pstream.mov", "pstream"),
        Website("Remark", "https://remark-gold.vercel.app", "remark"),
        Website("Hexa Watch", "https://hexa.watch", "hexawatch"),
        Website("Streames", "https://streamed.pk", "streamed"),
        Website("Hianime", "https://hianime.to", "hianime"),
        Website("AnimeKai", "https://animekai.ac/home", "animekai")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.post {
            val displayMetrics = resources.displayMetrics
            val screenWidth = recyclerView.width

            // feste Breite aus item_website.xml: 200dp
            val itemMinWidthPx = (140 * displayMetrics.density).toInt()

            // Berechnen, wie viele Spalten reinpassen
            val spanCount = (screenWidth / itemMinWidthPx).coerceAtLeast(1)

            // GridLayoutManager mit dynamischer Spaltenanzahl
            val layoutManager = GridLayoutManager(this, spanCount)
            recyclerView.layoutManager = layoutManager

            // Adapter setzen
            recyclerView.adapter = WebsiteAdapter(websites) { website ->
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", website.url)
                intent.putExtra("title", website.title)
                startActivity(intent)
            }

            // Gleichmäßige Abstände zwischen den Items
            val spacing = (screenWidth - spanCount * itemMinWidthPx) / (spanCount + 1)
            recyclerView.addItemDecoration(EqualSpacingItemDecoration(spacing, spanCount))
        }
    }
}
