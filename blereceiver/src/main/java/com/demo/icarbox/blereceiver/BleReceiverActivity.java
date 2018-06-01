package com.demo.icarbox.blereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.guiying.module.common.base.BaseActionBarActivity;
import com.guiying.module.common.utils.Utils;
import com.icarbonx.smartdevice.manager.ble.BleDevice;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

@Route(path = "/ble/receiver")
public class BleReceiverActivity extends BaseActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private EasyRecyclerView mEasyRecyclerView;
    private DeviceAdaper mDeviceAdaper;
    private Handler handler;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ScannerService.ACTION);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(broadcastReceiver);
    }

//    ScanBroadcastReceiver scanBroadcastReceiver = new ScanBroadcastReceiver();

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.e("receiver broad",context.getPackageName());
            if (intent.getAction().equals(ScannerService.ACTION)) {
                if (intent.getIntExtra(ScannerService.STATUS_I, -1) == ScannerService.STATUS.SINGLE) {
                    mDeviceAdaper.addDataByScanResult(intent.getParcelableExtra(ScanResult.class.getName()));
                } else if (intent.getIntExtra(ScannerService.STATUS_I, -1) == ScannerService.STATUS.BATCH) {
                    mDeviceAdaper.addDataByScanResults(intent.getParcelableArrayListExtra(ScanResult.class.getName()));
                } else {//scan failed

                }

            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.serviceStart) {
            Intent intent = new Intent(this, ScannerService.class);
            intent.putExtra(ScannerService.CONTROL_I, 1);
            ArrayList<ScanFilter> scanFilters = new ArrayList<>();
            scanFilters.add(new ScanFilter.Builder().setDeviceName("bong4").build());
            intent.putParcelableArrayListExtra(ScanFilter.class.getName(),scanFilters);

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
        public void addDataByScanResult(ScanResult scanResult) {
            BleDevice bleDevice = new BleDevice()
                    .addRssi(scanResult.getRssi())
                    .addMac(scanResult.getDevice().getAddress())
                    .addName(scanResult.getDevice().getName())
                    .addScanData(scanResult.getScanRecord().getBytes());

            int ind = mObjects.indexOf(bleDevice);
            if (ind > 0) {
                update(bleDevice, ind);
            } else {
                if(getCount()<20)add(bleDevice);
            }
        }

        /**
         * Add by scanresult set
         *
         * @param list
         */
        public void addDataByScanResults(List<ScanResult> list) {
            Iterator<ScanResult> iterator = list.iterator();
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
