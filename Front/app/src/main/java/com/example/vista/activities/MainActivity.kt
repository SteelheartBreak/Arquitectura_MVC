package com.example.vista.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vista.data.Usuario
import com.example.vista.databinding.ActivityMainBinding
import com.example.vista.interfaces.ApiService
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
            .baseUrl("http://localhost:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)


        binding.botonRegistrarse.setOnClickListener{
            val intent = Intent (this, RegistroActivity::class.java)
            startActivity(intent)
        }

        binding.botonIniciarSesion.setOnClickListener{
            usuario = binding.inputUsuario.text.toString()
            if (buscarUsuarioPorNombre(usuario)){
                val intent = Intent (this, APPActivity::class.java)
                startActivity(intent)
            }
            else{
                val toast = Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT)
                toast.show()
            }

        }

    }

    private fun buscarUsuarioPorNombre(nombreUsuario: String): Boolean {
        var flag = false
        val call = apiService.buscarUsuarioPorNombre(nombreUsuario)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    flag = response.body() == "Usuario encontrado"
                }
                else{
                    flag = false
                }
                }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.println(Log.ERROR,"No","API not up")
            }
        })
        return flag
    }
}