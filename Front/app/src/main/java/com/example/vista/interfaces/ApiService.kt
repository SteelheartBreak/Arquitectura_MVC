package com.example.vista.interfaces
import com.example.vista.data.Usuario
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/buscar/{nombre_usuario}")
    fun buscarUsuarioPorNombre(@Path("nombre_usuario") nombreUsuario: String): Call<Usuario>

    @GET("/eliminar/{nombre_usuario}")
    fun eliminarUsuarioPorNombre(@Path("nombre_usuario") nombreUsuario: String): Call<Usuario>

    @GET("/crear_usuario/{nombre_usuario}")
    fun crearUsuarioPorNombre(@Path("nombre_usuario") nombreUsuario: String): Call<Usuario>
}
