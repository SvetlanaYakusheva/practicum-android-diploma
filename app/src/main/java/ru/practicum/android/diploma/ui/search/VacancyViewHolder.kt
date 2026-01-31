package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyViewHolder(private val binding: VacancyItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Vacancy) {
        binding.apply {
            Glide.with(root)
                .load(model.employerLogoPath)
                .placeholder(R.drawable.ic_employer_logo_placeholder_48)
                .into(employerLogo)

            // добавить areaName в Vacancy
            vacancyName.text = "${model.name}, "
        }
    }

    companion object {
        fun from(parent: ViewGroup): VacancyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = VacancyItemBinding.inflate(inflater, parent, false)
            return VacancyViewHolder(binding)
        }
    }

}
