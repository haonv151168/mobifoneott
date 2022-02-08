package com.mobifone.mobifonecall

class Config {
    companion object {
        var socketUrl = "https://ott2.mobifone.ai/"
        var jwt_token = ""

        val EVENT_MISS = "EVENT_MISS";
        val EVENT_CANCEL = "EVENT_CANCEL";
        val EVENT_REJECT = "EVENT_REJECT";
        val EVENT_ACCEPT = "EVENT_ACCEPT";
        val EVENT_RINGING = "EVENT_RINGING";
        val EVENT_CALLING = "EVENT_CALLING";
        val EVENT_END = "EVENT_END";
        val EVENT_TERMINATED = "EVENT_TERMINATED";
    }
}