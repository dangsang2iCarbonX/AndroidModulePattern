package com.demo.icarbox.blereceiver;

import android.accounts.Account;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.guiying.module.common.base.BaseActionBarActivity;
import com.guiying.module.common.utils.Utils;
import com.icarbonx.smartdevice.ble.manager.BleDevice;
import com.icarbonx.smartdevice.ble.manager.BleScanDevice;
import com.icarbonx.smartdevice.bodyfat.BodyFatData;
import com.icarbonx.smartdevice.bodyfat.BodyFatDataHelper;
import com.icarbonx.smartdevice.bodyfat.BodyFatUser;
import com.icarbonx.smartdevice.http.BleHttpManager;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import no.nordicsemi.android.ble.Request;
import no.nordicsemi.android.log.LocalLogSession;
import no.nordicsemi.android.log.LogSession;
import no.nordicsemi.android.log.Logger;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;

@Route(path = "/ble/receiver")
public class BleReceiverActivity extends BaseActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String TAG = BleReceiverActivity.class.getName();

    private EasyRecyclerView mEasyRecyclerView;
    private DeviceAdaper mDeviceAdaper;
    private Handler handler;
    private BodyFatManager mBodyFatManager;

    //Body data helper helping parsing and callback.
    BodyFatDataHelper bodyFatDataHelper = new BodyFatDataHelper();
    BodyFatParser bodyFatParser = new BodyFatParser();
    @Override
    protected int setTitleId() {
        return R.string.str_ble_receiver_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_receiver);

        mEasyRecyclerView = findViewById(R.id.recyclerView);
        mEasyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEasyRecyclerView.setRefreshListener(this);

        mDeviceAdaper = new DeviceAdaper(this);
        mEasyRecyclerView.setAdapter(mDeviceAdaper);

        handler = new Handler(Looper.myLooper());

        findViewById(R.id.serviceStart).setOnClickListener(this);
        findViewById(R.id.serviceStop).setOnClickListener(this);

        mBodyFatManager = new BodyFatManager(this);
        mBodyFatManager.setGattCallbacks(mBodyFatManagerCallbacks);
        mBodyFatManager.setLogger(Logger.newSession(this, "test", "bodyfat"));
//        try {
//            BleHttpManager.getInstance().init(this);
////            BleScanManager.getInstance().init(this);
//        } catch (ICarbonXEception iCarbonXEception) {
//            iCarbonXEception.printStackTrace();
//        }

//        LayoutInflater.Factory
//        ThreadPoolExecutor
        bodyFatDataHelper.setmIDataParser(bodyFatParser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("", "onResume");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ScannerService.ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("", "onPause");
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(broadcastReceiver);
        mBodyFatManager.disconnect();
        mBodyFatManager.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("", "onStop");
    }

    class BodyFatParser extends BodyFatDataHelper.AbstractParser{
        @Override
        public void onBodyFatDataEndSending(BodyFatData bodyFatData) {
            Log.e(TAG,bodyFatData.toString());
        }

        @Override
        public void onChangingWeight(int weight) {
            Log.e(TAG,"weight changing "+weight);
        }
    }

    int controlSq = 0;
    BodyFatManagerCallbacks mBodyFatManagerCallbacks = new BodyFatManagerCallbacks() {
        @Override
        public void onReceive(byte[] data) {
            Log.e(TAG, "data received: " + BodyFatManager.byteArray2String(data));
            bodyFatDataHelper.parse(data);
        }

        @Override
        public void onDataSent(byte[] data) {
            Log.e(TAG, data.length + "data sent: " + BodyFatManager.byteArray2String(data));
            controlSq++;
            mBodyFatManager.setCrrentUser(
                    new BodyFatUser()
                            .setId(1)
                            .setAge(0x1b)
                            .setGender(1)
                            .setHeight(0xaa),controlSq
            );
        }

        @Override
        public void onDeviceReady(BluetoothDevice device) {
            Log.e(TAG, device.getAddress() + " onDeviceReady");
            controlSq++;
            mBodyFatManager.setCrrentUser(
                    new BodyFatUser()
                            .setId(1)
                            .setAge(0x1b)
                            .setGender(1)
                            .setHeight(0xaa),controlSq
            );

        }

        @Override
        public void onDeviceSleep() {
            Log.e(TAG, "device entry low power mode");
        }

        @Override
        public void onDeviceConnecting(BluetoothDevice device) {

        }

        @Override
        public void onDeviceConnected(BluetoothDevice device) {
            Log.e(TAG, device.getAddress() + " connected");
        }

        @Override
        public void onDeviceDisconnecting(BluetoothDevice device) {
            Log.e(TAG, device.getAddress() + " disconnecting");
        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device) {
            Log.e(TAG, device.getAddress() + " disconnected");
        }

        @Override
        public void onLinklossOccur(BluetoothDevice device) {

        }

        @Override
        public void onServicesDiscovered(BluetoothDevice device, boolean optionalServicesFound) {
            Log.e(TAG, device.getAddress() + " onServicesDiscovered");
        }


        @Override
        public boolean shouldEnableBatteryLevelNotifications(BluetoothDevice device) {
            return false;
        }

        @Override
        public void onBatteryValueReceived(BluetoothDevice device, int value) {

        }

        @Override
        public void onBondingRequired(BluetoothDevice device) {

        }

        @Override
        public void onBonded(BluetoothDevice device) {

        }

        @Override
        public void onError(BluetoothDevice device, String message, int errorCode) {
            Log.e(TAG, device.getAddress() + errorCode + message);
        }

        @Override
        public void onDeviceNotSupported(BluetoothDevice device) {
            Log.e(TAG, device.getAddress() + " device does not contain service demanded");
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("receive device", context.getPackageName());
            if (intent.getAction().equals(ScannerService.ACTION)) {
                if (intent.getIntExtra(ScannerService.STATUS_I, -1) == ScannerService.STATUS.SINGLE) {
                    BleScanDevice scanDevice = intent.getParcelableExtra(BleScanDevice.class.getName());
                    DeviceDataBean dataBean = new DeviceDataBean();
                    dataBean.setDeviceID(scanDevice.getMac());
                    dataBean.setAdvData(Utils.byteArray2String(scanDevice.getScanRawData()));

                    if (scanDevice.getName().equals("ICX-ABF")) {
                        mBodyFatManager.connect(BluetoothAdapter.getDefaultAdapter()
                                .getRemoteDevice(scanDevice.getMac()));
                        findViewById(R.id.serviceStop).callOnClick();
                    }
//                    BleHttpManager.getInstance()
//                            .addDataBody(new Gson().toJson(dataBean, DeviceDataBean.class))
//                            .addIBleHttpResult(iBleResult)
//                            .startRequest();

                    mDeviceAdaper.addDataByScanResult(scanDevice);
                } else if (intent.getIntExtra(ScannerService.STATUS_I, -1) == ScannerService.STATUS.FAILURE) {
//                    mDeviceAdaper.addDataByScanResults(intent.getParcelableArrayListExtra(ScanResult.class.getName()));
                }
            }
        }
    };

    private BleHttpManager.IBleHttpResult iBleResult = new BleHttpManager.IBleHttpResult() {

        @Override
        public void onSuccess(long callID, String responseBody) {
            Log.e(TAG, "upload successfully" + callID);
        }

        @Override
        public void onFail(long callID) {
            Log.e(TAG, "upload failed" + callID);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.serviceStart) {
            Intent intent = new Intent(this, ScannerService.class);
            intent.putExtra(ScannerService.CONTROL_I, 1);

            ArrayList<ScanFilter> scanFilters = new ArrayList<>();
            scanFilters.add(new ScanFilter.Builder().setDeviceName("bong4").build());
            intent.putParcelableArrayListExtra(ScanFilter.class.getName(), scanFilters);

            startService(intent);
        } else if (v.getId() == R.id.serviceStop) {
            Intent intent = new Intent(this, ScannerService.class);
            intent.putExtra(ScannerService.CONTROL_I, -1);
            stopService(intent);
        }
    }

    @Override
    public void onRefresh() {
        Log.e("receiver", "add one deive");
        mDeviceAdaper.add(new BleDevice()
                .addRssi((int) (1 + Math.random() * (10 - 1 + 1)))
                .addMac("DS")
                .addName("iCarbonX")
                .addScanData("scan data a lot data".getBytes())
        );
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEasyRecyclerView.setRefreshing(false);
            }
        }, 1000);
    }

    private class DeviceAdaper extends RecyclerArrayAdapter<BleDevice> {

        public DeviceAdaper(Context context) {
            super(context);
        }

        /**
         * Add by one scanresult
         *
         * @param scanResult
         */
        public void addDataByScanResult(BleScanDevice scanResult) {
            Log.e("rssi", scanResult.getRssi() + "[" + scanResult.getName() + "]" + scanResult.getMac());
            BleDevice bleDevice = new BleDevice()
                    .addRssi(scanResult.getRssi())
                    .addMac(scanResult.getMac())
                    .addName(scanResult.getName())
                    .addScanData(scanResult.getScanRawData());

            int ind = mObjects.indexOf(bleDevice);
            if (ind > 0) {
                update(bleDevice, ind);
            } else {
                if (getCount() < 20) add(bleDevice);
            }
        }

        /**
         * Add by scanresult set
         *
         * @param list
         */
        public void addDataByScanResults(List<BleScanDevice> list) {
            Iterator<BleScanDevice> iterator = list.iterator();
            while (iterator.hasNext()) {
                addDataByScanResult(iterator.next());
            }
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new DeviceViewHolder(parent);
        }
    }

    private class DeviceViewHolder extends BaseViewHolder<BleDevice> {
        private TextView mTV_rssi;
        private TextView mTV_mac;
        private TextView mTV_name;
        private TextView mTV_scanData;


        public DeviceViewHolder(ViewGroup parent) {
            super(parent, R.layout.ble_device_item);
            mTV_rssi = $(R.id.ble_rssi);
            mTV_mac = $(R.id.ble_mac);
            mTV_name = $(R.id.ble_name);
            mTV_scanData = $(R.id.ble_scanData);
        }

        @Override
        public void setData(BleDevice data) {
            mTV_rssi.setText(String.format("%4d", data.getRssi()));
            mTV_mac.setText(data.getMac());
            mTV_name.setText(data.getName());
            mTV_scanData.setText(Utils.byteArray2String(data.getScanData()));
        }
    }
}
