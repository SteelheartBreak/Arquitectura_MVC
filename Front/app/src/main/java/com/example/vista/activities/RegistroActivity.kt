package com.example.vista.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vista.data.Usuario
import com.example.vista.databinding.ActivityRegistroBinding
import com.example.vista.interfaces.ApiService
import com.google.gson.JsonObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.43.100.80:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.backButtonR.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.botonRegistrarseR.setOnClickListener{
            if (validateForm()){
                val usuario = binding.inputUsuarioR.text.toString()
                crearUsuarioPorNombre(usuario)
            }
            else{
                val toast = Toast.makeText(this, "Llena todas las casillas", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun validateForm(): Boolean {
        val usuario = binding.inputUsuarioR.text.toString()
        val nombre = binding.inputNombreR.text.toString()

        return usuario.isNotEmpty() && nombre.isNotEmpty()
    }

    private fun crearUsuarioPorNombre(nombreUsuario: String) {
        val call = apiService.crearUsuarioPorNombre(nombreUsuario)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    val mensaje = jsonObject?.get("message")?.asString
                    if (mensaje == "Usuario creado correctamente") {
                        val toast = Toast.makeText(
                            this@RegistroActivity,
                            "Usuario creado correctamente",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        val toast = Toast.makeText(
                            this@RegistroActivity,
                            "Usuario ya existente",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(
                        this@RegistroActivity,
                        "Usuario ya existente",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("RegistroActivity", "Error en la solicitud: ${t.message}")
                val toast = Toast.makeText(
                    this@RegistroActivity,
                    "Error en la solicitud: ${t.message}",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        })
    }
}