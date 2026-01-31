package ru.practicum.android.diploma.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(
    private val onVacancyClick: (String) -> Unit
) : RecyclerView.Adapter<VacancyViewHolder>() {

    private var vacancyList: List<Vacancy> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacancyViewHolder = VacancyViewHolder.from(parent, onVacancyClick)

    override fun onBindViewHolder(
        holder: VacancyViewHolder,
        position: Int
    ) {
        holder.bind(vacancyList[position])
    }

    override fun getItemCount(): Int = vacancyList.size

    fun updateList(newList: List<Vacancy>) {
        vacancyList = newList
        notifyDataSetChanged()
    }
}
