package com.example.schub

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class WebsiteAdapter(
    private val items: List<Website>,
    private val onClick: (Website) -> Unit
) : RecyclerView.Adapter<WebsiteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.websiteTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_website, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val website = items[position]
        val context = holder.itemView.context

        val name = website.backgroundImageName

        if (!name.isNullOrBlank()) {
            val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
            if (resId != 0) {
                // PNG gefunden
                val bitmap = BitmapFactory.decodeResource(context.resources, resId)
                val roundedDrawable = RoundedBitmapDrawableFactory.create(context.resources, bitmap)
                val density = context.resources.displayMetrics.density
                roundedDrawable.cornerRadius = 20 * density // 20dp
                holder.itemView.background = roundedDrawable
            } else {
                // PNG-Name angegeben, aber Datei fehlt → Fallback
                holder.itemView.setBackgroundResource(R.drawable.background_website_item)
                holder.title.text = website.title
            }
        } else {
            // Kein Name angegeben → Fallback
            holder.itemView.setBackgroundResource(R.drawable.background_website_item)
            holder.title.text = website.title
        }

        // Fokus-Animation
        holder.itemView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).start()
            } else {
                view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start()
            }
        }

        holder.itemView.setOnClickListener { onClick(website) }
    }
}
