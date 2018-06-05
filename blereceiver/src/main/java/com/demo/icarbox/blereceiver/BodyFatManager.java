package com.demo.icarbox.blereceiver;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.icarbonx.smartdevice.ble.manager.BleGattManager;
import com.icarbonx.smartdevice.ble.manager.BleGattManagerCallbacks;

import java.lang.reflect.Array;
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
            if(characteristic == mNotyCharacteric)
                dataChanged(gatt,characteristic);
        }

        @Override
        public void onCharacteristicNotified(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            if(characteristic == mNotyCharacteric)
                dataChanged(gatt,characteristic);
        }

        private void dataChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            //BL send response
            final byte[] data = characteristic.getValue();
            if (data.length == 8 && data[7] == 0xcc && data[2] == 0xfe) {
                mCallbacks.onDeviceSleep();
                return;
            }

            mCallbacks.onReceive(data);
        }
    };


    /**
     * Add user to device.
     *
     * @param userId     User ID
     * @param gender     User gender,1 for male,0 for female
     * @param age        User age since birth.
     * @param height     User body height in cm.
     * @param weight     User weight before.
     * @param impedance  User impendace of body.
     * @param userId2    Second user ID.
     * @param gender2    Second user gender,1 for male,0 for female
     * @param age2       Second user age since birth.
     * @param height2    Second user body height in cm.
     * @param weight2    Sencond user weight before.
     * @param impedance2 Second user impendace of body.
     */
    void addUser(int userId, int gender, int age, int height, int weight, int impedance,
                 int userId2, int gender2, int age2, int height2, int weight2, int impedance2) {
        //18bytes for two users
        byte[] data = new byte[4+8+8];
        data[0] = (byte) 0xac;
        data[1] = 2;
        data[2] = (byte) 0xfd;
        data[3] = 0;//添加用户信息标识
        data[4] = (byte) userId;
        data[5] = (byte) gender;
        data[6] = (byte) age;
        data[7] = (byte) height;
        data[8] = (byte) (weight >> 8);
        data[9] = (byte) weight;
        data[10] = (byte) ((byte) impedance >> 8);
        data[11] = (byte) impedance;
        data[12] = (byte) userId2;
        data[13] = (byte) gender2;
        data[14] = (byte) age2;
        data[15] = (byte) height2;
        data[16] = (byte) (weight2 >> 8);
        data[17] = (byte) weight2;
        data[18] = (byte) ((byte) impedance2 >> 8);
        data[19] = (byte) impedance2;

        mWriteCharactertic.setValue(data);
        Log.e(Tag, "start addUser");
        writeCharacteristic(mWriteCharactertic);
    }

    /**
     * Syn user after {@link #addUser(int, int, int, int, int, int, int, int, int, int, int, int)}.
     */
    void synUser() {
        byte[] data = {(byte) 0xac, 2, (byte) 0xfd, 02, 00, 00, (byte) 0xcf, (byte) 0xce};
        mWriteCharactertic.setValue(data);
        Log.e(Tag, "addUser ended");
        writeCharacteristic(mWriteCharactertic);
    }

    /**
     * Set current user to use.
     *
     * @param userId User ID
     * @param gender User gender,1 for male,0 for female
     * @param age    User age since birth.
     * @param height User body height in cm.
     */
    void setCurrentUser(int userId, int gender, int age, int height) {
        //When set current user, send data twice. One for user id, the other for other info,
        byte[] data = {(byte) 0xAC, 02, 0, 0, 0, 0, (byte) 0xcc, 0};
        data[2] = (byte) 0xfa;
        data[3] = (byte) userId;
        data[7] = sumCheck(data, 0, data.length - 1);

        mWriteCharactertic.setValue(data);
        writeCharacteristic(mWriteCharactertic);

        byte[] data2 = {(byte) 0xAC, 02, 0, 0, 0, 0, (byte) 0xcc, 0};
        data2[2] = (byte) 0xfb;
        data2[3] = (byte) gender;
        data2[4] = (byte) age;
        data2[5] = (byte) height;

        data2[7] = sumCheck(data2, 0, data2.length - 1);
        mWriteCharactertic.setValue(data2);
        writeCharacteristic(mWriteCharactertic);
    }

    byte sumCheck(byte[] data, int offset, int dest) {
        int v = 0;
        for (int i = offset; i < dest; i++) {
            v = v + (data[i] & 0xff);
        }

        return (byte) v;
    }
}
