package ru.practicum.android.diploma.di

import android.net.Network
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase

object Key {
    const val BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"
}

val dataModule = module {

    single { androidContext() }

    single<VacanciesAPI> {
        Retrofit.Builder()
            .baseUrl(Key.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VacanciesAPI::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient()
    }

    factory { Gson() }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "database.db")
            .build()
    }

}
