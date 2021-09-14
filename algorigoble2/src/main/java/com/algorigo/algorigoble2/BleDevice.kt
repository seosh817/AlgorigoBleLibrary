package com.algorigo.algorigoble2

import android.util.Log
import java.util.UUID

class BleDevice() {

    enum class ConnectionState(var status: String) {
        CONNECTING("CONNECTING"),
        CONNECTED("CONNECTED"),
        DISCONNECTED("DISCONNECTED"),
        DISCONNECTING("DISCONNECTING")
    }

    internal lateinit var engine: BleDeviceEngine

    val deviceId: String
        get() = engine.deviceId

    val deviceName: String?
        get() = engine.deviceName

    val bondState: Int
        get() = engine.bondState

    val connected: Boolean
        get() = engine.isConnected()

    val connectionState: ConnectionState
        get() = engine.getConnectionStateObservable().blockingFirst()

    fun bondCompletable() = engine.bondCompletable()

    fun getConnectionStateObservable() = engine.getConnectionStateObservable()
    fun connectCompletable() = connectCompletableImpl()
    internal open fun connectCompletableImpl() = engine.connectCompletable()

    fun connect() {
        connectCompletable().subscribe({
            Log.e(TAG, "connected")
        }, {
            Log.e(TAG, "conntection fail", it)
        })
    }

    fun disconnect() = engine.disconnect()

    fun readCharacteristicSingle(characteristicUuid: UUID) =
        engine.readCharacteristicSingle(characteristicUuid)
    fun writeCharacteristicSingle(characteristicUuid: UUID, byteArray: ByteArray) =
        engine.writeCharacteristicSingle(characteristicUuid, byteArray)

    override fun toString(): String {
        return "$deviceName($deviceId)"
    }

    override fun equals(other: Any?): Boolean {
        return other is BleDevice && this.deviceId == other.deviceId
    }

    companion object {
        private val TAG = BleDevice::class.java.simpleName
    }
}
