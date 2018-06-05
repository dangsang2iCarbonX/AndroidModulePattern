package com.icarbonx.smartdevice.ble.manager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.BleManagerCallbacks;
import no.nordicsemi.android.log.ILogSession;

/**
 * <p>
 * The BleManager is responsible for managing the low level communication with a Bluetooth LE device.
 * Please see profiles implementation in Android nRF Blinky or Android nRF Toolbox app for an
 * example of use.
 * <p>
 * This base manager has been tested against number of devices and samples from Nordic SDK.
 * <p>
 * The manager handles connection events and initializes the device after establishing the connection.
 * <ol>
 * <li>For bonded devices it ensures that the Service Changed indications, if this characteristic
 * is present, are enabled. Android does not enable them by default, leaving this to the
 * developers.</li>
 * <li>The manager tries to read the Battery Level characteristic. No matter the result of this
 * operation (for example the Battery Level characteristic may not have the READ property)
 * it tries to enable Battery Level notifications, to get battery updates from the device.</li>
 * <li>Afterwards, the manager initializes the device using given queue of commands.
 * See {@link BleManagerGattCallback#initGatt(BluetoothGatt)} method for more details.</li>
 * <li>When initialization complete, the {@link BleGattManagerCallbacks#onDeviceReady(BluetoothDevice)}
 * callback is called.</li>
 * </ol>
 * The manager also is responsible for parsing the Battery Level values and calling
 * {@link BleGattManagerCallbacks#onBatteryValueReceived(BluetoothDevice, int)} method.
 * <p>
 * If {@link #setLogger(ILogSession)} was called, the events are logged into the nRF Logger
 * application, which may be downloaded from Google Play:
 * <a href="https://play.google.com/store/apps/details?id=no.nordicsemi.android.log">https://play.google.com/store/apps/details?id=no.nordicsemi.android.log</a>
 * <p>
 * The nRF Logger application allows you to see application logs without need to connect it to the computer.
 * <p>
 * The BleManager should be overridden in your app and all the 'high level' callbacks should be called from there.
 * Keeping this file as is (and {@link BleGattManagerCallbacks} as well) will allow to quickly update it when an update is posted here.
 *
 * @param <E> The profile callbacks type
 */
public abstract class BleGattManager<E extends BleGattManagerCallbacks> extends BleManager<E> {
    protected static final String Tag = BleGattManager.class.getSimpleName();

    /**
     * The manager constructor.
     * <p>
     * After constructing the manager, the callbacks object must be set with
     * {@link #setGattCallbacks(BleGattManagerCallbacks)} .
     * <p>
     * To connect a device, call {@link #connect(BluetoothDevice)}.
     *
     * @param context context
     */
    public BleGattManager(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setGattCallbacks(@NonNull E callbacks) {
        super.setGattCallbacks(callbacks);
    }
}
