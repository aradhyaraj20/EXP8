package com.example.exp8

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.card.MaterialCardView

class ImageAdapter(
    private val context: Context,
    private val items: List<ImageItem>,
    private val onItemClick: (ImageItem) -> Unit,
    private val onPickLocalImage: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: MaterialCardView = view.findViewById(R.id.cardView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val checkOverlay: ImageView = view.findViewById(R.id.checkOverlay)
        val badgeSource: TextView = view.findViewById(R.id.badgeSource)
        val textTitle: TextView = view.findViewById(R.id.textTitle)
        val btnMenu: ImageButton = view.findViewById(R.id.btnMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = items[position]
        holder.textTitle.text = item.title

        if (item.isSelected) {
            holder.cardView.strokeColor = Color.parseColor("#10B981")
            holder.cardView.strokeWidth = 6
            holder.checkOverlay.visibility = View.VISIBLE
        } else {
            holder.cardView.strokeColor = Color.parseColor("#1E293B")
            holder.cardView.strokeWidth = 2
            holder.checkOverlay.visibility = View.GONE
        }

        when (item.sourceType) {
            ImageSourceType.DRAWABLE -> {
                holder.badgeSource.text = "1. DRAWABLE"
                Glide.with(context)
                    .load(item.drawableResId)
                    .centerCrop()
                    .into(holder.imageView)
            }
            ImageSourceType.LOCAL_STORAGE -> {
                holder.badgeSource.text = "2. LOCAL"
                Glide.with(context)
                    .load(item.filePath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.imageView)
            }
            ImageSourceType.URL -> {
                holder.badgeSource.text = "3. URL/URI"
                Glide.with(context)
                    .load(item.url)
                    .placeholder(R.drawable.sample_drawable_3)
                    .error(R.drawable.sample_drawable_4)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.imageView)
            }
        }

        holder.itemView.setOnClickListener {
            item.isSelected = !item.isSelected
            notifyItemChanged(position)
            onItemClick(item)
        }

        holder.btnMenu.setOnClickListener { view ->
            showPopupMenu(view, item, position)
        }

        holder.itemView.setOnLongClickListener { view ->
            showPopupMenu(view, item, position)
            true
        }
    }

    fun selectAll() {
        items.forEach { it.isSelected = true }
        notifyDataSetChanged()
    }

    private fun showPopupMenu(view: View, item: ImageItem, position: Int) {
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

        // Add "Pick from Device Storage" option for LOCAL_STORAGE items
        if (item.sourceType == ImageSourceType.LOCAL_STORAGE) {
            popup.menu.add(0, 999, 0, "Pick Photo from Device")
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                999, R.id.action_edit -> {
                    if (item.sourceType == ImageSourceType.LOCAL_STORAGE) {
                        onPickLocalImage?.invoke(position)
                    } else {
                        Toast.makeText(context, "Editing image: ${item.title}", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                R.id.action_select_all -> {
                    selectAll()
                    Toast.makeText(context, "All ${items.size} images selected!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_share -> {
                    Toast.makeText(context, "Sharing image: ${item.title}", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun getItemCount(): Int = items.size
}
