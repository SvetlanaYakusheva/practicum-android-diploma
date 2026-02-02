package ru.practicum.android.diploma.ui.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyViewHolder(
    private val binding: VacancyItemBinding,
    private val onVacancyClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Vacancy) {
        binding.apply {
            Glide.with(root)
                .load(model.employerLogoPath)
                .placeholder(R.drawable.ic_employer_logo_placeholder_48)
                .into(employerLogo)

            vacancyName.text = root.context.getString(
                R.string.view_holder_vacancy_name, model.name, model.addressCity
            )

            root.setOnClickListener {
                onVacancyClick(model.id)
            }
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onVacancyClick: (String) -> Unit
        ): VacancyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = VacancyItemBinding.inflate(inflater, parent, false)
            return VacancyViewHolder(binding, onVacancyClick)
        }
    }
}
