package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.VacanciesAPI

val dataModule = module {

    single<VacanciesAPI> {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VacanciesAPI::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    factory { Gson() }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "database.db")
            .build()
    }

}
