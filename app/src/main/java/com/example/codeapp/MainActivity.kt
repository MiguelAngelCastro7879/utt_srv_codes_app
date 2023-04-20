package com.example.codeapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.CallSuper
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.codeapp.databinding.ActivityMainBinding
import com.example.codeapp.services.LoginService.LoginResponse
import com.example.codeapp.services.LoginService.LoginService
import com.example.codeapp.services.LoginService.QrResponse
import com.example.codeapp.services.LoginService.QrService
import com.example.codeapp.services.Singleton
import com.google.zxing.integration.android.IntentIntegrator
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var singleton:Singleton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {

//                Toast.makeText(this, "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
//                Toast.makeText(this, singleton.value , Toast.LENGTH_LONG).show()
                consumeService(singleton.value);
//                Toast.makeText(this, Singleton.getValue() , Toast.LENGTH_LONG).show()

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun consumeService(token: String): MutableLiveData<LoginResponse> {
        val liveData = MutableLiveData<LoginResponse>()
        var url = "https://miguelacv.online";
//        val switchh: Switch = binding.online;
//        if(!switchh.isChecked){
        url = "http://10.20.0.4"
//        }
        val response = this.buildService(QrService::class.java, url)
        response.validateCode("Bearer $token").enqueue(
            object : Callback<QrResponse> {
                override fun onResponse(
                    call: Call<QrResponse>,
                    response: Response<QrResponse>
                ) {
                    Toast.makeText(this@MainActivity, "Sesion iniciada correctamente" , Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<QrResponse>, t: Throwable) {
                    Log.d("ERRRRRROOOOOOOR",t.toString());
                    Toast.makeText(this@MainActivity,"ARREGLA TUS ERRORES", Toast.LENGTH_LONG).show()
                }

            }
        )
        return liveData
    }

    private fun<T> buildService(service: Class<T>, url: String): T{
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(service)
    }
}