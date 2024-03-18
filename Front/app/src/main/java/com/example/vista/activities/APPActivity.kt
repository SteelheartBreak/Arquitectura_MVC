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
import com.google.gson.JsonObject
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
            .baseUrl("http://10.43.100.80:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.botonEliminar.setOnClickListener{
            val usuario = binding.inputUsuario.text.toString()
            eliminarUsuarioPorNombre(usuario)
        }
    }

    private fun eliminarUsuarioPorNombre(nombreUsuario: String) {
        val call = apiService.eliminarUsuarioPorNombre(nombreUsuario)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    val mensaje = jsonObject?.get("message")?.asString
                    if (mensaje == "Usuario eliminado correctamente") {
                        val toast = Toast.makeText(
                            this@APPActivity,
                            "Usuario eliminado correctamente",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    } else {
                        val toast = Toast.makeText(
                            this@APPActivity,
                            "Usuario no encontrado",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(
                        this@APPActivity,
                        "Usuario no encontrado",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("MainActivity", "Error en la solicitud: ${t.message}")
                val toast = Toast.makeText(
                    this@APPActivity,
                    "Error en la solicitud: ${t.message}",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        })
    }
}