package ru.practicum.android.diploma.data.network

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

    companion object {
        const val HTTP_NO_CONNECTION = -1
        const val HTTP_CLIENT_ERROR = 400
        const val HTTP_SERVER_ERROR = 500
        const val HTTP_SUCCESS = 200
    }
}
