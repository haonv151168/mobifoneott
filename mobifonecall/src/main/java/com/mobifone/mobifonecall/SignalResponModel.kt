package com.mobifone.mobifonecall

import org.json.JSONObject

data class SignalResponModel(
    var r: Int? = null,
    var data: DataResponModel? = null,
    var error: String? = null
) {
    data class DataResponModel(
        var request_id: String? = null,
        var room_id: String? = null,
        var from_user: String? = null,
        var from_user_socket: String? = null,
        var to_user_socket: String? = null,
        var to_user: String? = null,
        var is_hotline: String? = null,
        var to_hotline: String? = null
    )
}

class Factory {
    companion object {
        private fun createDataResponseModel(json: JSONObject): SignalResponModel.DataResponModel {
            val data = SignalResponModel.DataResponModel()
            data.request_id = json.getString("request_id")
            data.room_id = json.getString("room_id")
            data.from_user = json.getString("from_user")
            data.from_user_socket = json.getString("from_user_socket")
            data.to_user_socket = json.getString("to_user_socket")
            data.to_user = json.getString("to_user")
            data.is_hotline = json.getString("is_hotline")
            data.to_hotline = json.getString("to_hotline")
            return data
        }

        fun createSignalResponModel(json: JSONObject): SignalResponModel {
            val data = SignalResponModel()
            data.r = json.getInt("r")
            data.data = createDataResponseModel(json.getJSONObject("data"))
            data.error = json.getString("error")
            return data
        }
    }
}
