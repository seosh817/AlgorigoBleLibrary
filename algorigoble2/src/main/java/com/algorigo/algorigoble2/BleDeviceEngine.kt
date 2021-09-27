package com.algorigo.algorigoble2

import android.bluetooth.BluetoothGattCharacteristic
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.UUID

abstract class BleDeviceEngine {

    lateinit var bleDevice: BleDevice

    abstract val deviceId: String
    abstract val deviceName: String?
    abstract val bonded: Boolean

    protected val connectionStateRelay: BehaviorRelay<BleDevice.ConnectionState> = BehaviorRelay.create<BleDevice.ConnectionState>().apply { accept(BleDevice.ConnectionState.DISCONNECTED) }

    abstract fun bondCompletable(): Completable

    fun isConnected(): Boolean = getConnectionStateObservable().firstOrError().map { it == BleDevice.ConnectionState.CONNECTED }.blockingGet()
    fun getConnectionStateObservable(): Observable<BleDevice.ConnectionState> = connectionStateRelay

    abstract fun connectCompletable(): Completable

    abstract fun disconnect()

    abstract fun getCharacteristicsSingle(): Single<List<BluetoothGattCharacteristic>>

    abstract fun readCharacteristicSingle(characteristicUuid: UUID): Single<ByteArray>
    abstract fun writeCharacteristicSingle(characteristicUuid: UUID, byteArray: ByteArray): Single<ByteArray>
    abstract fun setupNotification(characteristicUuid: UUID, byteArray: ByteArray): Observable<Observable<ByteArray>>

    abstract fun connectSppSocket(uuid: UUID? = null): Observable<BleSppSocket>
}
