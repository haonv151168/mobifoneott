package com.mobifone.mobifonecall

interface CallListener {
    fun onSignalingStateChange(state: String, model: SignalResponModel?)
    fun onError(message: String?)
}