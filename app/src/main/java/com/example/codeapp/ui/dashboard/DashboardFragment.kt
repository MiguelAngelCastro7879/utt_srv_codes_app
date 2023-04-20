package com.example.codeapp.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.codeapp.databinding.FragmentDashboardBinding
import com.example.codegen.APIService
import com.example.codegen.CodeResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.ToDoubleBiFunction

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard

        val txtCode:TextView = binding.code;

        val btnSend:TextView = binding.send;
        val inpCode:EditText = binding.input;

        btnSend.setOnClickListener(View.OnClickListener {
            val requestBody = mapOf("application_code" to inpCode.text.toString())
            consumeService(requestBody, txtCode)
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun consumeService(request: Map<String, String>, textView: TextView): MutableLiveData<CodeResponse> {
        val liveData = MutableLiveData<CodeResponse>()
        var url = "https://miguelacv.online";
        val switchh:Switch = binding.online;
        if(!switchh.isChecked){
            url = "http://10.20.0.4"
        }
        val response = this.buildService(APIService::class.java, url)
        response.validateCode(request).enqueue(
            object : Callback<CodeResponse> {
                override fun onResponse(
                    call: Call<CodeResponse>,
                    response: Response<CodeResponse>
                ) {
                    if (response.body()?.getLogincode() != null){
                        textView.text = response.body()?.getLogincode()
                        liveData.value = response.body()
                    }else{
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Codigo Invalido")
                        builder.setMessage("\nFavor de ingresar un código valido\n")
                        builder.setPositiveButton(android.R.string.yes) { dialog, which -> }
                        builder.show()
                    }
                }
                override fun onFailure(call: Call<CodeResponse>, t: Throwable) {
                    Log.d("ERRRRRROOOOOOOR",t.toString());
                    Toast.makeText(context,"ARREGLA TUS ERRORES",Toast.LENGTH_LONG).show()
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