package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class RetrofitNetworkClient(
    private val context: Context,
    private val vacanciesService: VacanciesAPI,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NetworkClient.HTTP_NO_CONNECTION }
        }

        return withContext(defaultDispatcher) {
            try {
                when (dto) {
                    is VacanciesSearchRequest -> vacanciesService.getVacancies(dto.toMap())
                        .apply { resultCode = NetworkClient.HTTP_SUCCESS }

                    is VacancyRequest -> VacancyResponse(vacanciesService.getVacancyFull(dto.vacancyId))
                        .apply { resultCode = NetworkClient.HTTP_SUCCESS }

                    is AreasRequest -> AreaResponse(vacanciesService.getAreas())
                        .apply { resultCode = NetworkClient.HTTP_SUCCESS }

                    is IndustriesRequest -> IndustriesResponse(vacanciesService.getIndustries())
                        .apply { resultCode = NetworkClient.HTTP_SUCCESS }

                    else -> Response().apply { resultCode = NetworkClient.HTTP_CLIENT_ERROR }
                }
            } catch (e: HttpException) {
                Response().apply { resultCode = e.code() }
            } catch (_: SocketTimeoutException) {
                Response().apply { resultCode = NetworkClient.HTTP_SERVER_ERROR }
            } catch (_: IOException) {
                Response().apply { resultCode = NetworkClient.HTTP_NO_CONNECTION }
            } catch (_: Exception) {
                Response().apply { resultCode = NetworkClient.HTTP_SERVER_ERROR }
            }
        }
    }

    private fun VacanciesSearchRequest.toMap(): Map<String, String> {
        return buildMap {
            if (text.isNotEmpty()) put("text", text)
            if (page > 0) put("page", page.toString())
            if (perPage > 0) put("per_page", perPage.toString())

            filter.region?.let { put("area", it.id) }
            filter.industry?.let { put("industry", it.id) }
            filter.salary?.let { put("salary", it) }
            put("only_with_salary", filter.onlyWithSalary.toString())
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }
}
