package com.example.vista.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vista.data.Usuario
import com.example.vista.databinding.ActivityRegistroBinding
import com.example.vista.interfaces.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private lateinit var apiService: ApiService
    var usuario =""
    var nombre = ""
    var flag = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8000")
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
                if(crearUsuarioPorNombre(usuario)){
                    val toast = Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT)
                    toast.show()
                    val intent = Intent (this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val toast = Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT)
                    toast.show()
                }
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

    private fun crearUsuarioPorNombre(nombreUsuario: String):Boolean{
        var flag = false
        val call = apiService.crearUsuarioPorNombre(nombreUsuario)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val respuesta = response.body().toString()
                    flag = respuesta == "Usuario creado correctamente"
                }
                else{
                    Log.println(Log.ERROR,"No","No encontrado")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.println(Log.ERROR,"No","API not up")
            }
        })
        return flag
    }
}