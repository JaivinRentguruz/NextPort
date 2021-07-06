package com.flexiicar.app.backgroundtasks

interface AcuantTokenServiceListener {
    fun onSuccess(token: String)
    fun onFail(responseCode: Int)
}