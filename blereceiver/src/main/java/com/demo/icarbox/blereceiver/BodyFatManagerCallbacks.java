package com.demo.icarbox.blereceiver;

import android.bluetooth.BluetoothGattCharacteristic;

import com.icarbonx.smartdevice.ble.manager.BleGattManagerCallbacks;

import java.util.UUID;

public interface BodyFatManagerCallbacks extends BleGattManagerCallbacks {
    /**
     * Received response data.
     *
     * @param data Byte array data of BL device.
     */
    void onReceive(byte[] data);

    /**
     * Called when the data has been sent to the connected device.
     *
     * @param data The data sent.
     */
    void onDataSent(byte[] data);

    /**
     * Device entry low power model.
     */
    void onDeviceSleep();

}
