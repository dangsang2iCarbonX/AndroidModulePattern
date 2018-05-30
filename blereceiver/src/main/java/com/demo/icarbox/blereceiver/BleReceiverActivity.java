package com.demo.icarbox.blereceiver;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.guiying.module.common.utils.Utils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

@Route(path = "/ble/receiver")
public class BleReceiverActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private EasyRecyclerView mEasyRecyclerView;
    private DeviceAdaper mDeviceAdaper;
    private Handler handler;

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
    }

    @Override
    public void onRefresh() {
        Log.e("receiver", "add one deive");
        mDeviceAdaper.add(new BleDevice()
                .Rssi((int) (1 + Math.random() * (10 - 1 + 1)))
                .Mac("DS")
                .Name("iCarbonX")
                .ScanData("scan data a lot data".getBytes())
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
