package ru.practicum.android.diploma.ui.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.OnItemClickListener
import ru.practicum.android.diploma.util.UtilFunctions

class VacancyViewHolder(itemView: View, private val onClickListener: OnItemClickListener) :
    ListItemViewHolder(itemView) {

    private val context = itemView.context

    private val vacancyIcon = itemView.findViewById<ImageView>(R.id.employer_logo)
    private val vacancyTitle = itemView.findViewById<TextView>(R.id.vacancy_name)
    private val companyName = itemView.findViewById<TextView>(R.id.employer_name)
    private val salaryRange = itemView.findViewById<TextView>(R.id.vacancy_salary)

    override fun bind(vacancy: Vacancy) {
        itemView.setOnClickListener { onClickListener.onItemClick(vacancy.id) }
        Glide.with(context)
            .load(vacancy.employerLogoPath)
            .placeholder(R.drawable.ic_employer_logo_placeholder_48)
            .centerInside()
            .transform(RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.dimen_12dp)))
            .into(vacancyIcon)

        vacancyTitle.text =
            String.format(context.getString(R.string.view_holder_vacancy_name), vacancy.name, vacancy.addressCity)

        companyName.text = vacancy.employerName

        salaryRange.text = UtilFunctions.formatSalary(vacancy, context)
    }

}
