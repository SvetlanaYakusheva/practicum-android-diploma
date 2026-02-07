package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.Mapper
import ru.practicum.android.diploma.data.network.AreaResponse
import ru.practicum.android.diploma.data.network.AreasRequest
import ru.practicum.android.diploma.data.network.IndustriesRequest
import ru.practicum.android.diploma.data.network.IndustriesResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.DictionariesRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.ErrorType
import ru.practicum.android.diploma.util.Resource

class DictionariesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: Mapper
) : DictionariesRepository {

    override fun getAreas(): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.doRequest(AreasRequest())
        emit(
            when (response.resultCode) {
                NetworkClient.HTTP_SUCCESS -> {
                    val result = with(mapper) {
                        (response as AreaResponse).area.map { it.toArea() }
                    }
                    Resource.Success(result)
                }
                NetworkClient.HTTP_NO_CONNECTION -> Resource.Error(ErrorType.NoConnection)
                NetworkClient.HTTP_NOTHING_FOUND -> Resource.Error(ErrorType.NothingFound)
                else -> Resource.Error(ErrorType.ServerError)
            }
        )
    }

    override fun getRegionsFlatMap(): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.doRequest(AreasRequest())
        emit(
            when (response.resultCode) {
                NetworkClient.HTTP_SUCCESS -> {
                    val result = with(mapper) {
                        (response as AreaResponse).area.map { it.toArea() }
                    }

                    val allAreas = with(mapper) {result.flatMap { it.getAllNodes() }}

                    Resource.Success(allAreas)
                }

                NetworkClient.HTTP_NO_CONNECTION -> Resource.Error(ErrorType.NoConnection)
                NetworkClient.HTTP_NOTHING_FOUND -> Resource.Error(ErrorType.NothingFound)
                else -> Resource.Error(ErrorType.ServerError)
            }
        )
    }

    override fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = networkClient.doRequest(IndustriesRequest())
        emit(
            when (response.resultCode) {
                NetworkClient.HTTP_SUCCESS -> {
                    val result = with(mapper) {
                        (response as IndustriesResponse).industries.map { it.toIndustry() }
                    }
                    Resource.Success(result)
                }
                NetworkClient.HTTP_NO_CONNECTION -> Resource.Error(ErrorType.NoConnection)
                else -> Resource.Error(ErrorType.ServerError)
            }
        )
    }
}
