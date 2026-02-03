package ru.practicum.android.diploma.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.domain.models.Phone
import ru.practicum.android.diploma.ui.viewholders.PhoneViewHolder

class PhoneAdapter(
    private val onPhoneClick: (String) -> Unit
) : RecyclerView.Adapter<PhoneViewHolder>() {

    private var phoneList: List<Phone> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhoneViewHolder = PhoneViewHolder.from(parent, onPhoneClick)

    override fun onBindViewHolder(
        holder: PhoneViewHolder,
        position: Int
    ) {
        holder.bind(phoneList[position])
    }

    override fun getItemCount(): Int = phoneList.size

    fun updateList(newList: List<Phone>) {
        phoneList = newList
        notifyDataSetChanged()
    }
}
