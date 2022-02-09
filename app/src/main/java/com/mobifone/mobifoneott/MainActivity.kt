package com.mobifone.mobifoneott

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.webrtcdemoandroid.repository.ApiRepository
import com.example.webrtcdemoandroid.repository.ApiRepositoryImpl
import com.mobifone.mobifonecall.Config
import com.mobifone.mobifonecall.MeetingInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var edtUserName: EditText
    lateinit var edtHotline: EditText
    lateinit var btnLogin: Button

    var apiRepository: ApiRepository = ApiRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtUserName = findViewById(R.id.edt_username)
        edtHotline = findViewById(R.id.edt_hotline)
        btnLogin = findViewById(R.id.btn_login)

        edtUserName.setText("thanh")
        edtHotline.setText("18001234")

        btnLogin.setOnClickListener {
            GlobalScope.launch {
                val response = apiRepository.getToken(edtUserName.text.toString())
                if (response.status == "200") {
                    Log.e("tag", response.token)
                    Config.jwt_token = response.token
                    MeetingInfo.toHotline = edtHotline.text.toString()

                    // navigate screen
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}