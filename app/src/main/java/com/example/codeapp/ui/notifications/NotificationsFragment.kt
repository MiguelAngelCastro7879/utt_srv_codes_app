package com.example.codeapp.ui.notifications

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.codeapp.MainActivity
import com.example.codeapp.databinding.FragmentNotificationsBinding
import com.google.zxing.integration.android.IntentIntegrator

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
                ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        binding.btnScanner.setOnClickListener { initScanner() }
        return root
    }

    private fun initScanner(){
        val integrator = IntentIntegrator(activity)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}