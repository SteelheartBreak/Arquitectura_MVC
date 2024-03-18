package com.example.vista.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vista.data.Usuario
import com.example.vista.databinding.ActivityMainBinding
import com.example.vista.interfaces.ApiService
import com.google.gson.JsonObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var binding: ActivityMainBinding
    var usuario = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.43.100.80:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.botonRegistrarse.setOnClickListener{
            val intent = Intent (this, RegistroActivity::class.java)
            startActivity(intent)
        }

        binding.botonIniciarSesion.setOnClickListener{
            usuario = binding.inputUsuario.text.toString()
            buscarUsuarioPorNombre(usuario)
        }

    }

    private fun buscarUsuarioPorNombre(nombreUsuario: String) {
        val call = apiService.buscarUsuarioPorNombre(nombreUsuario)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    val mensaje = jsonObject?.get("message")?.asString
                    if (mensaje == "Usuario encontrado") {
                        val intent = Intent(this@MainActivity, APPActivity::class.java)
                        startActivity(intent)
                    } else {
                        val toast = Toast.makeText(
                            this@MainActivity,
                            "Usuario no encontrado",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(
                        this@MainActivity,
                        "Usuario no encontrado",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("MainActivity", "Error en la solicitud: ${t.message}")
                val toast = Toast.makeText(
                    this@MainActivity,
                    "Error en la solicitud: ${t.message}",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        })
    }

}