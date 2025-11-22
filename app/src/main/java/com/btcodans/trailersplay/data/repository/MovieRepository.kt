package com.btcodans.trailersplay.data.repository

import com.btcodans.trailersplay.data.network.ApiService
import com.btcodans.trailersplay.data.network.RetrofitClient

class MovieRepository {
    private val api = RetrofitClient.instance.create(ApiService::class.java)
    private val apiKey = "b10f2c762703b5fb0dcecf42212f083c"

    suspend fun getPopularMovies() = api.getPopularMovies(apiKey)

    suspend fun getMovieTrailers ( movieId: Int ) = api.getMovieTrailers(movieId,apiKey)
    suspend fun searchMovies(query: String) = api.searchMovies(apiKey, query)

}
