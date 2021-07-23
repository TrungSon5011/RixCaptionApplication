package com.nui.rixcaption.util

import android.util.Log
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler

class ExecuteBinaryResponse(): ExecuteBinaryResponseHandler() {

    override fun onStart() {
        Log.i("ExecuteResponse","starting")
    }

    override fun onFailure(message: String?) {
        Log.i("ExecuteResponseFailure","$message")
    }

    override fun onProgress(message: String?) {
        Log.i("onProgress","$message")
    }

    override fun onSuccess(message: String?) {
        Log.i("ExecuteResponseOnSucces","$message")

    }
    override fun onFinish() {
        Log.i("ExecuteResponseOnFN","Finished")
    }
}