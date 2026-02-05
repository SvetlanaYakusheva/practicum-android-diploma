package ru.practicum.android.diploma.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.viewholders.VacancyViewHolder
import ru.practicum.android.diploma.util.OnItemClickListener

class VacancyAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<VacancyViewHolder>() {

    val listVacancies = mutableListOf<Vacancy>()

    fun setData(newListData: List<Vacancy>) {
        listVacancies.clear()
        listVacancies.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VacancyViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.vacancy_list_item, parent, false)
    ) { id -> onItemClickListener.onItemClick(id) }

    override fun getItemCount(): Int = listVacancies.size

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(listVacancies[position])
    }
}
