package ru.practicum.android.diploma.ui.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Area

class AreaViewHolder(
    parent: ViewGroup,
    private val onItemClickListener: (Area) -> Unit

) : ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.area_list_item, parent, false)
) {

    val name: TextView = itemView.findViewById(R.id.area_name)

    fun bind(area: Area) {
        name.text = area.name
        itemView.setOnClickListener { onItemClickListener(area) }
    }
}
