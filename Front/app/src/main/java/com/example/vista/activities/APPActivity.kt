package com.example.vista.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vista.data.Usuario
import com.example.vista.databinding.ActivityAppactivityBinding
import com.example.vista.databinding.ActivityRegistroBinding
import com.example.vista.interfaces.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APPActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var binding: ActivityAppactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.botonEliminar.setOnClickListener{
            val usuario = binding.inputUsuario.text.toString()
            if(eliminarUsuarioPorNombre(usuario)){
                val toast = Toast.makeText(this, "Usuario eliminado exitosamente", Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                val toast = Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun eliminarUsuarioPorNombre(nombreUsuario: String):Boolean{
        var flag = false
        val call = apiService.eliminarUsuarioPorNombre(nombreUsuario)
        call.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                }
                else{
                    Log.println(Log.ERROR,"No","No encontrado")
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Log.println(Log.ERROR,"No","API not up")
            }
        })
        return flag
    }
}