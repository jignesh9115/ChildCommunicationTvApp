package com.jv.demotvapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var btnInfoMom: Button
    private lateinit var btnInfoDad: Button
    private lateinit var edtMom: EditText
    private lateinit var edtDad: EditText
    private val TAG: String = javaClass.simpleName
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAcMH-Xwc:APA91bETrfs_K0a1muzhCAS9zdVjIn6dMQoJvt90DPuKdBQynMFd5vhg67F_P5wSHgtINsD1Q1SkVMu0QVwqgt859YlmTGIgtt0GZFipQoJiEdPUDu7MXVeyPZuzMxIQI54FVR_eY83k"
    private val contentType = "application/json"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInfoMom = findViewById(R.id.btn_mom)
        edtMom = findViewById(R.id.edt_mom_msg)
        btnInfoDad = findViewById(R.id.btn_dad)
        edtDad = findViewById(R.id.edt_dad_msg)

        FirebaseMessaging.getInstance().subscribeToTopic("all").addOnCompleteListener { task ->
            var msg = "Subscribed"
            if (!task.isSuccessful) {
                msg = "Subscribe failed"
            }
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }

        btnInfoMom.setOnClickListener {
            if (edtMom.text.isNotEmpty()) {
                makeNotification("/topics/Mummy", "Alert For Mummy", edtMom.text.toString())
                edtMom.setText("")
                Toast.makeText(applicationContext, "Inform Mummy !", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(applicationContext, "Please Enter Message", Toast.LENGTH_SHORT)
                .show()
        }

        btnInfoDad.setOnClickListener {
            if (edtDad.text.isNotEmpty()) {
                makeNotification("/topics/Papa", "Alert For Papa", edtDad.text.toString())
                edtDad.setText("")
                Toast.makeText(applicationContext, "Inform Papa !", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(applicationContext, "Please Enter Message", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun makeNotification(topic: String, title: String, msg: String) {
        val notification = JSONObject()
        val notificationBody = JSONObject()

        try {
            notificationBody.put("title", title)
            notificationBody.put("message", msg)   //Enter your notification message
            notification.put("to", topic)
            notification.put("data", notificationBody)
        } catch (e: JSONException) {
            Log.e("TAG", "onCreate: " + e.message)
        }
        sendNotification(notification)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e(TAG, "sendNotification")
        val jsonObjectRequest = object :
            JsonObjectRequest(Method.POST, FCM_API, notification, Response.Listener { response ->
                Log.i(TAG, "onResponse: $response")
            }, Response.ErrorListener {
                Toast.makeText(this@MainActivity, "Request error", Toast.LENGTH_LONG).show()
                Log.i(TAG, "onErrorResponse: Didn't work " + it.message)
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }
}