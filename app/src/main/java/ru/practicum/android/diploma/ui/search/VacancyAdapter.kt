package ru.practicum.android.diploma.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter : RecyclerView.Adapter<VacancyViewHolder>() {

    private var vacancyList: List<Vacancy> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacancyViewHolder = VacancyViewHolder.from(parent)

    override fun onBindViewHolder(
        holder: VacancyViewHolder,
        position: Int
    ) {
        holder.bind(vacancyList[position])
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }

    fun updateList(newList: List<Vacancy>) {
        vacancyList = newList
        notifyDataSetChanged()
    }

}
