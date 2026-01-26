package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.VacanciesAPI

val dataModule = module {

    single<VacanciesAPI> {
        Retrofit.Builder()
            .baseUrl(Key.BASE_URL)
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

object Key {
    const val BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"
}
