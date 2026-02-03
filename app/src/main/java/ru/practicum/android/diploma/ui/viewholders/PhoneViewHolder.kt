package ru.practicum.android.diploma.ui.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.PhoneItemBinding
import ru.practicum.android.diploma.domain.models.Phone

class PhoneViewHolder(
    private val binding: PhoneItemBinding,
    private val onPhoneClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Phone) {
        binding.apply {
            phoneNumber.text = model.formatted
            phoneComment.text = model.comment

            root.setOnClickListener {
                if (model.formatted != null) {
                    onPhoneClick(model.formatted)
                }
            }
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onPhoneClick: (String) -> Unit
        ): PhoneViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PhoneItemBinding.inflate(inflater, parent, false)
            return PhoneViewHolder(binding, onPhoneClick)
        }
    }
}

