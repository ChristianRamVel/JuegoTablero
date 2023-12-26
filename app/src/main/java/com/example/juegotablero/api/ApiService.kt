package com.example.juegotablero.api

import com.example.juegotablero.model.Pregunta
import retrofit2.http.GET
import retrofit2.Call

interface ApiService {
    // Definición del método para obtener publicaciones. Utiliza la anotación @GET para indicar
    // que es una solicitud GET HTTP. La función devuelve un objeto Call que encapsula una lista de Posts.
    @GET("posts")
    fun getPosts(): Call<List<Pregunta>>
}
