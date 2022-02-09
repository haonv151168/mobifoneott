package com.mobifone.mobifoneott

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.mobifone.mobifonecall.*

class HomeActivity : AppCompatActivity(), MobifoneHelperListener, CallListener {

    lateinit var btnCall: Button
    lateinit var ringing: ConstraintLayout
    lateinit var tvName : TextView
    lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnCall = findViewById(R.id.btn_call)
        ringing = findViewById(R.id.ringing)
        tvName = findViewById(R.id.tv_name)
        btnCancel = findViewById(R.id.btn_cancel)

        Config.socketUrl = "https://ott.mobifone.ai/"

        ContextHolder.context = applicationContext
        MobifoneClient.mobifoneHelperListener = this
        MobifoneClient.callListener = this

        if (!MobifoneClient.isConnected()) {
            MobifoneClient.connectServer()
        }

        btnCall.setOnClickListener {
            MobifoneClient.makeCall(MeetingInfo.toHotline, "", null, "c2h")
        }

        btnCancel.setOnClickListener {
            MobifoneClient.cancelCall()
        }
    }

    override fun onConnectionConnect() {
        Log.e("tag", "onConnectionConnect")
    }

    override fun onConnectionError() {
        Log.e("tag", "onConnectionError")
    }

    override fun onSignalingStateChange(state: String, model: SignalResponModel?) {
        Log.e("tag", "state $state")

        when (state) {
            Config.EVENT_CALLING -> {
                runOnUiThread {
                    tvName.text = MeetingInfo.toHotline
                    ringing.visibility = View.VISIBLE
                    btnCall.visibility = View.GONE
                }
            }

            Config.EVENT_RINGING -> {
                // ignore
            }

            Config.EVENT_MISS -> {
                hideRinging()
            }

            Config.EVENT_REJECT -> {
                hideRinging()
            }

            Config.EVENT_CANCEL -> {
                hideRinging()
            }

            Config.EVENT_ACCEPT -> {
                hideRinging()
                MobifoneClient.joinMeeting()
            }

            Config.EVENT_END -> {
                MobifoneClient.closeMeeting()
            }

            Config.EVENT_TERMINATED -> {

            }
        }
    }

    override fun onError(message: String?) {
        Log.e("tag", "onError " + message.toString())
    }

    private fun hideRinging() {
        runOnUiThread {
            btnCall.visibility = View.VISIBLE
            ringing.visibility = View.GONE
        }
    }

}