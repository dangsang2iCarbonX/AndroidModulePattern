package com.icarbonx.smartdevice.ble.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import no.nordicsemi.android.ble.BleManager;
import no.nordicsemi.android.ble.BleManagerCallbacks;

/**
 * Extends once to expand dafault base methods.
 * <p>
 * The BleManagerCallbacks should be overridden in your app and all the 'high level' callbacks
 * should be added there. See examples in Android nRF Blinky or Android nRF Toolbox.
 * </p>
 * <p>
 * Keeping this file as is (and {@link BleGattManager} as well) will allow to quickly update it when
 * an update is posted here.
 *
 * @author lavi
 */
public interface BleGattManagerCallbacks extends BleManagerCallbacks {

}
