package ru.practicum.android.diploma.data.network

import android.content.Context

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HeadHunterApi
) : NetworkClient
