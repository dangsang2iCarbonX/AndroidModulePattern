package com.demo.icarbox.blereceiver;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.guiying.module.common.utils.Utils;
import com.icarbonx.smartdevice.ble.manager.BleGattManager;
import com.icarbonx.smartdevice.ble.manager.BleGattManagerCallbacks;
import com.icarbonx.smartdevice.bodyfat.BodyFatCmds;
import com.icarbonx.smartdevice.bodyfat.BodyFatUser;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

import no.nordicsemi.android.ble.Request;

import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE;

public class BodyFatManager extends BleGattManager<BodyFatManagerCallbacks> {
    /**
     * Nordic Blinky Service UUID.
     */
    public final static UUID BODY_FAT_UUID_SERVICE = UUID.fromString("0000ffb0-0000-1000-8000-00805f9b34fb");
    /**
     * BUTTON characteristic UUID.
     */
    private final static UUID BODY_FAT_UUID_WRITE_CHAR = UUID.fromString("0000ffb1-0000-1000-8000-00805f9b34fb");
    /**
     * LED characteristic UUID.
     */
    private final static UUID BODY_FAT_UUID_NOTY_CHAR = UUID.fromString("0000ffb2-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic mWriteCharactertic, mNotyCharacteric;

    /**
     * The manager constructor.
     * <p>
     * After constructing the manager, the callbacks object must be set with
     * {@link #setGattCallbacks(BleGattManagerCallbacks)} .
     * <p>
     * To connect a device, call {@link }.
     *
     * @param context context
     */
    public BodyFatManager(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected BleManagerGattCallback getGattCallback() {
        return mBleManagerGattCallback;
    }


    private final BleManagerGattCallback mBleManagerGattCallback = new BleManagerGattCallback() {
        @Override
        protected boolean isRequiredServiceSupported(BluetoothGatt gatt) {
            final BluetoothGattService service = gatt.getService(BODY_FAT_UUID_SERVICE);
            Log.e("service demand", "" + service);
            if (service != null) {
                mWriteCharactertic = service.getCharacteristic(BODY_FAT_UUID_WRITE_CHAR);
                mNotyCharacteric = service.getCharacteristic(BODY_FAT_UUID_NOTY_CHAR);
            }
            Log.e("service demand", "" + mWriteCharactertic);
            boolean writeRequest = false;
            if (mWriteCharactertic != null) {
                final int rxProperties = mWriteCharactertic.getProperties();
                writeRequest = (rxProperties & (BluetoothGattCharacteristic.PROPERTY_WRITE | PROPERTY_WRITE_NO_RESPONSE)) > 0;
            }
            Log.e("service demand", "" + writeRequest + mWriteCharactertic.getProperties());
            return mWriteCharactertic != null && mNotyCharacteric != null && writeRequest;
        }

        @Override
        protected Deque<Request> initGatt(BluetoothGatt gatt) {
            final LinkedList<Request> requests = new LinkedList<>();
            requests.push(Request.newEnableNotificationsRequest(mNotyCharacteric));
            return requests;
        }

        @Override
        protected void onDeviceDisconnected() {

        }

        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            //Every write operation will invoke this
            final byte[] data = characteristic.getValue();
            mCallbacks.onDataSent(data);
        }

        @Override
        protected void onCharacteristicIndicated(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic == mNotyCharacteric)
                dataChanged(gatt, characteristic);
        }

        @Override
        public void onCharacteristicNotified(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            if (characteristic == mNotyCharacteric)
                dataChanged(gatt, characteristic);
        }

        private void dataChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            //BL send response
            final byte[] data = characteristic.getValue();
//            Log.e("rv", "" + data.length + "RV" + byteArray2String(data));

            if (data != null && data.length == 8 && data[6] == (byte) 0xcc && data[2] == (byte) 0xfe && data[3] == 1) {
                mCallbacks.onDeviceSleep();
                return;
            }

            mCallbacks.onReceive(data);
        }
    };

    /**
     * Set device the current user
     *
     * @param bodyFatUser {@link BodyFatUser}
     */
    public void setCrrentUser(BodyFatUser bodyFatUser, int state) {
        //同步用户列表
        //添加同步指令
        if (state == 1) {
            enqueue(Request.newWriteRequest(mWriteCharactertic, new BodyFatCmds().getAddUserInfo(bodyFatUser)));
        }

        //添加同步结束指令
        if (state == 2) {
            enqueue(Request.newWriteRequest(mWriteCharactertic, new BodyFatCmds().getAddUserEnd()));
        }

        //添加设置当前用户其他信息指令
        if (state == 3) {
            enqueue(Request.newWriteRequest(mWriteCharactertic, new BodyFatCmds().getUserId(bodyFatUser)));
        }

        //添加设置当前用户id指令
        if (state == 4) {
            enqueue(Request.newWriteRequest(mWriteCharactertic, new BodyFatCmds().getUserOther(bodyFatUser)));
        }
    }


    public void setCurrentUserId(BodyFatUser bodyFatUser) {

    }

    public void synUser() {

    }

    public void addUsers(BodyFatUser bodyFatUser) {

    }

    /**
     * Data checking by sum, and use last byte.
     */
    private byte sumCheck(byte[] data, int offset, int length) {
        int v = 0;
        for (int i = offset; i < length + offset; i++) {
            v = v + (data[i] & 0xff);
        }

        return (byte) v;
    }

    public static String byteArray2String(byte[] data) {
        if (data == null) return "";

        final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1)
                stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
