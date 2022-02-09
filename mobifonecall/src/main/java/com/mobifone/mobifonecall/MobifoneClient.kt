package com.mobifone.mobifonecall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.net.toUri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import org.mobifone.meet.sdk.*
import timber.log.Timber
import java.net.URL


object MobifoneClient {
    var mobifoneHelperListener: MobifoneHelperListener? = null
    var callListener: CallListener? = null

    lateinit var socket: Socket

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    init {
        val auth = HashMap<String, String>()
        auth["jwt"] = Config.jwt_token
        val options = IO.Options()
        options.auth = auth

        try {
            socket = IO.socket(Config.socketUrl, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        registerForBroadcastMessages()
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(ContextHolder.context).registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.getType()) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i("Conference Joined with url%s", event.getData().get("url"))
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s", event.getData().get("name"))
                BroadcastEvent.Type.CONFERENCE_TERMINATED -> {
                    endCall()
                    callListener?.onSignalingStateChange(Config.EVENT_TERMINATED, null)
                }
            }
        }
    }

    fun connectServer() {
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            Log.e("MobifoneClient", "onConnect in plugin")
            mobifoneHelperListener?.onConnectionConnect()
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("MobifoneClient", "onDisconnect in plugin")
            mobifoneHelperListener?.onConnectionError()
        }

        socket.on("NewCall:Response") {
            val responsePayload = it[0] as JSONObject
            var model = Factory.createSignalResponModel(responsePayload)
            when (model.r) {
                0 -> {
                    callListener?.onError(model.error)
                }
                1 -> {
                    MeetingInfo.fromUser = model.data?.from_user.toString()
                    MeetingInfo.toUser = model.data?.to_user.toString()
                    MeetingInfo.toHotline = model.data?.to_hotline.toString()

                    if (MeetingInfo.requestId == model.data?.request_id) {
                        callListener?.onSignalingStateChange(Config.EVENT_CALLING, model)
                    } else {
                        MeetingInfo.requestId = model.data?.request_id.toString()
                        callListener?.onSignalingStateChange(Config.EVENT_RINGING, model)
                    }
                }
                2 -> {
                    MeetingInfo.requestId = model.data?.request_id.toString()
                    MeetingInfo.roomId = model.data?.room_id.toString()
                    MeetingInfo.fromUser = model.data?.from_user.toString()
                    MeetingInfo.toUser = model.data?.to_user.toString()

                    callListener?.onSignalingStateChange(Config.EVENT_ACCEPT, model)
                }
                3 -> {
                    callListener?.onSignalingStateChange(Config.EVENT_REJECT, model)
                }
                4 -> {
                    callListener?.onSignalingStateChange(Config.EVENT_MISS, model)
                }
                5 -> {
                    callListener?.onSignalingStateChange(Config.EVENT_CANCEL, model)
                }
                6 -> {
                    MeetingInfo.requestId = model.data?.request_id.toString()
                }
            }

        }

        socket.on("CallEnded") {
            callListener?.onSignalingStateChange(Config.EVENT_END, null)
        }
    }

    fun isConnected(): Boolean {
        return socket.connected()
    }

    fun joinMeeting() {
        val options = MobifoneMeetConferenceOptions.Builder()
            .setRoom(MeetingInfo.roomId)
            .setServerURL(URL(MeetingInfo.serverString))
            .setToken(MeetingInfo.tokenString)
            // Settings for audio and video
            //.setAudioMuted(true)
            //.setVideoMuted(true)
            .build()
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        MobifoneMeetActivity.launch(ContextHolder.context, options)
    }

    fun closeMeeting() {
        hangUp()
    }

    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(org.webrtc.ContextUtils.getApplicationContext()).sendBroadcast(hangupBroadcastIntent)
    }

    fun makeCall(to_hotline_code: String?, custom_data: Any, to_user: String?, call_type: String) {
        val payload = JSONObject()
        payload.put("to_hotline_code", to_hotline_code)
        payload.put("custom_data", custom_data)
        payload.put("to_user", to_user)
        payload.put("call_type", call_type)
        socket.emit("NewCall", payload)
    }

    fun cancelCall() {
        val payload = JSONObject()
        payload.put("request_id", MeetingInfo.requestId)
        socket.emit("CancelCall", payload)
    }

    fun rejectCall() {
        val payload = JSONObject()
        payload.put("request_id", MeetingInfo.requestId)
        socket.emit("RejectCall", payload)
    }

    fun endCall() {
        Log.e("MobifoneClient", "endCall")
        socket.emit("EndCall")
    }

    fun acceptCall() {
        val payload = JSONObject()
        payload.put("request_id", MeetingInfo.requestId)
        socket.emit("AcceptCall", payload)
    }
}