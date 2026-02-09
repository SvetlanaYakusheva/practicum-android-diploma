package ru.practicum.android.diploma.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.viewholders.AreaViewHolder

class AreaAdapter(private val onItemClickListener: (Area?) -> Unit) :
    RecyclerView.Adapter<AreaViewHolder>() {

    private val listData = mutableListOf<Area>()
    private var selectedItem: Area? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder =
        AreaViewHolder(parent, onItemClickListener)

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    fun setData(areaList: List<Area>) {
        listData.clear()
        listData.addAll(areaList)
        if (!listData.contains(selectedItem)) {
            selectedItem = null
        }
        onItemClickListener(selectedItem)
        notifyDataSetChanged()
    }

}
