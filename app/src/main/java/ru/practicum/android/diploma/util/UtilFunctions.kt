package ru.practicum.android.diploma.util

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.DecimalFormat

object UtilFunctions {

    fun <T> debounce(
        delayMillis: Long,
        coroutineScope: CoroutineScope,
        useLastParam: Boolean,
        action: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            if (useLastParam) {
                debounceJob?.cancel()
            }
            if (debounceJob?.isCompleted != false || useLastParam) {
                debounceJob = coroutineScope.launch {
                    delay(delayMillis)
                    action(param)
                }
            }
        }
    }

    fun formatSalary(vacancy: Vacancy, context: Context): String {
        val symbol = vacancy.salaryCurrencyName
        val decimalFormat = DecimalFormat("#,###.##")
        val formattedString: String

        if (vacancy.salaryFrom == null && vacancy.salaryTo == null) {
            formattedString =
                context.getString(R.string.salary_not_specified)
        } else if (vacancy.salaryFrom != null && vacancy.salaryTo != null) {
            formattedString = String.format(
                context.getString(R.string.salary_range_from_to),
                decimalFormat.format(vacancy.salaryFrom),
                decimalFormat.format(vacancy.salaryTo),
                symbol
            )
        } else if (vacancy.salaryFrom != null) {
            formattedString = String.format(
                context.getString(R.string.salary_range_from),
                decimalFormat.format(vacancy.salaryFrom),
                symbol
            )
        } else {
            formattedString = String.format(
                context.getString(R.string.salary_range_to),
                decimalFormat.format(vacancy.salaryTo),
                symbol
            )
        }

        return formattedString
    }
}
