package com.jv.parentapp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.jv.parentapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this@MainActivity, "parentData")

        if (prefManager.getPrefString("Subscribed").isNotEmpty()) {
            invisibleViews()
            if (prefManager.getPrefString("Subscribed") == "Papa")
                binding.tv.text = "You Subscribed as Papa"
            else
                binding.tv.text = "You Subscribed as Mummy"
        }

        binding.btnSubmit.setOnClickListener {

            if (binding.rbDad.isChecked) {
                prefManager.setPrefString("Subscribed", "Papa")
                FirebaseMessaging.getInstance().subscribeToTopic("Papa")
                    .addOnCompleteListener { task ->
                        var msg = "Subscribed as Papa"
                        if (!task.isSuccessful) {
                            msg = "Subscribe failed"
                        }
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }
                binding.tv.text = "You Subscribed as Papa"
                invisibleViews()
            } else if (binding.rbMom.isChecked) {
                prefManager.setPrefString("Subscribed", "Mummy")
                FirebaseMessaging.getInstance().subscribeToTopic("Mummy")
                    .addOnCompleteListener { task ->
                        var msg = "Subscribed as Mummy"
                        if (!task.isSuccessful) {
                            msg = "Subscribe failed"
                        }
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }
                binding.tv.text = "You Subscribed as Mummy"
                invisibleViews()
            } else
                Snackbar.make(binding.root, "Who are you ?", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun invisibleViews() {
        binding.rgSelection.visibility=View.GONE
        binding.btnSubmit.visibility=View.GONE
    }
}