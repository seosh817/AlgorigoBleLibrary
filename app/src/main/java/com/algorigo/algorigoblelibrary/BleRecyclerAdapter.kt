package com.algorigo.algorigoblelibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.algorigo.algorigoble2.BleDevice

class BleRecyclerAdapter(private val bleRecyclerListener: BleRecyclerListener) : RecyclerView.Adapter<BleRecyclerAdapter.BleRecyclerViewHolder>() {

    interface BleRecyclerListener {
        fun onSelect(bleDevice: BleDevice)
        fun onBindButton(bleDevice: BleDevice)
        fun onConnectButton(bleDevice: BleDevice)
        fun onConnectSppButton(bleDevice: BleDevice)
    }

    inner class BleRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.title_view)
        private val bindBtn: Button = itemView.findViewById(R.id.bond_btn)
        private val connectBtn: Button = itemView.findViewById(R.id.connect_btn)
        private val connectSppBtn: Button = itemView.findViewById(R.id.connect_spp_btn)

        init {
            itemView.setOnClickListener {
                bleDeviceList?.get(adapterPosition)?.let {
                    bleRecyclerListener.onSelect(it)
                }
            }
            bindBtn.setOnClickListener {
                bleDeviceList?.get(adapterPosition)?.let {
                    bleRecyclerListener.onBindButton(it)
                }
            }
            connectBtn.setOnClickListener {
                bleDeviceList?.get(adapterPosition)?.let {
                    bleRecyclerListener.onConnectButton(it)
                }
            }
            connectSppBtn.setOnClickListener {
                bleDeviceList?.get(adapterPosition)?.let {
                    bleRecyclerListener.onConnectSppButton(it)
                }
            }
        }

        fun setData(bleDevice: BleDevice?) {
            bleDevice?.let {
                titleView.text = it.toString()
                if (it.bonded) {
                    bindBtn.text = "Bonded"
                    bindBtn.isEnabled = false
                } else {
                    bindBtn.text = "Bond"
                    bindBtn.isEnabled = true
                }
                when (it.connectionState) {
                    BleDevice.ConnectionState.CONNECTING -> {
                        connectBtn.isEnabled = false
                        connectBtn.text = itemView.context.getString(R.string.connecting, it.connectionState.status)
                    }
                    BleDevice.ConnectionState.CONNECTED -> {
                        connectBtn.isEnabled = true
                        connectBtn.setText(R.string.disconnect)
                    }
                    BleDevice.ConnectionState.DISCONNECTED -> {
                        connectBtn.isEnabled = true
                        connectBtn.setText(R.string.connect)
                    }
                    BleDevice.ConnectionState.DISCONNECTING -> {
                        connectBtn.isEnabled = false
                        connectBtn.setText(R.string.disconnecting)
                    }
                }
            }
        }
    }

    var bleDeviceList: List<BleDevice>? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleRecyclerViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_ble_device_list, parent, false).let {
            BleRecyclerViewHolder(it)
        }
    }

    override fun getItemCount(): Int {
        return bleDeviceList?.size ?: 0
    }

    override fun onBindViewHolder(vh: BleRecyclerViewHolder, position: Int) {
        vh.setData(bleDeviceList?.get(position))
    }
}