package com.example.codeapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.codeapp.MainActivity
import com.example.codeapp.databinding.FragmentHomeBinding
import com.example.codeapp.services.LoginService.LoginResponse
import com.example.codeapp.services.LoginService.LoginService
import com.example.codeapp.services.Singleton
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var myActivity: MainActivity
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val txtMail:EditText = binding.email
        val txtPass:EditText = binding.password
        val btnLogin: Button = binding.login



        btnLogin.setOnClickListener(View.OnClickListener {
            val requestBody = HashMap<String, String>()
            requestBody["email"] = txtMail.text.toString()
            requestBody["password"] = txtPass.text.toString()
            myActivity = context as MainActivity
//

            consumeService(requestBody)
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun consumeService(request: HashMap<String, String>): MutableLiveData<LoginResponse> {
        val liveData = MutableLiveData<LoginResponse>()
//        var url = "https://miguelacv.online";
//        val switchh: Switch = binding.online;
//        if(!switchh.isChecked){
        val url = "http://10.20.0.4"
//        }
        val response = this.buildService(LoginService::class.java, url)
        response.validateCode(request).enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.body()?.access_token != null){
                        myActivity.singleton = Singleton.getInstance(response.body()?.access_token!!)!!
                        Toast.makeText(context, response.body()?.access_token, Toast.LENGTH_SHORT).show()

                    }else{
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Error")
                        builder.setMessage("\nError al iniciar sesion\n")
                        builder.setPositiveButton(android.R.string.yes) { dialog, which -> }
                        builder.show()
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("ERRRRRROOOOOOOR",t.toString());
                    Toast.makeText(context,"ARREGLA TUS ERRORES", Toast.LENGTH_LONG).show()
                }

            }
        )
        return liveData
    }

    fun<T> buildService(service: Class<T>, url: String): T{
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(service)
    }
}