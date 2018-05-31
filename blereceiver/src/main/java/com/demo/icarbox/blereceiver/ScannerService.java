package com.demo.icarbox.blereceiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/**
 * Use original class name as key and class object as value.
 * Send data though localBroadcastManager
 */
public class ScannerService extends Service {
    private final static String TAG = ScannerService.class.getName();
    public static final String STATUS_I = TAG + "status";
    public static final String CONTROL_I = TAG + "control";
    public static final String ACTION = TAG + "broadcast_action";

    //Manager to send data
    private LocalBroadcastManager mLocalBroadcastManager;
    private boolean isScanning = false;

    /**
     * Status code for scan result.
     */
    public static class STATUS{
        public final static int SINGLE = 1;
        public final static int BATCH = 2;
        public final static int ERROR = -1;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        BleManager.getInstance(getApplicationContext());

        Log.e(TAG, "onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e(TAG, "onStart");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand ");
        if (intent == null) return super.onStartCommand(intent, flags, startId);

        int controlCode = intent.getIntExtra(CONTROL_I, -1);
        Log.e(TAG, "onStartCommand " + controlCode);
        if (controlCode == -1) {
            if (isScanning) {
                Log.e(TAG, "onStartCommand stop scan" + controlCode);
                BluetoothLeScannerCompat.getScanner().stopScan(scanCallback);
                isScanning = false;
            }
            stopSelf(startId);
            return super.onStartCommand(intent, flags, startId);
        }
        if (controlCode == 1 && (isScanning==false)) {
            ArrayList<ScanFilter> filters = intent.getParcelableArrayListExtra(ScanFilter.class.getName());
            ScanSettings scanSettings = intent.getParcelableExtra(ScanSettings.class.getName());

            if (scanSettings == null) {
                scanSettings = new ScanSettings.Builder()
                        .setLegacy(false)
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0)
                        .setUseHardwareBatchingIfSupported(false).build();
            }
            Log.e(TAG, "onStartCommand: startScan");
            isScanning = true;
            BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            scanner.startScan(filters, scanSettings, scanCallback);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(isScanning){
            BluetoothLeScannerCompat.getScanner().stopScan(scanCallback);
        }
//        mLocalBroadcastManager = null;
        Log.e(TAG, "onDestroy");
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //Log.e(TAG, result.getDevice().getAddress()+"="+result.getDevice().getName());

            mLocalBroadcastManager.sendBroadcast(new Intent()
                    .setAction(ACTION)
                    .putExtra(ScanResult.class.getName(), result)
                    .putExtra(STATUS_I,STATUS.SINGLE)
            );

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.e(TAG, "onBatchScanResults");
            mLocalBroadcastManager.sendBroadcast(new Intent()
                    .setAction(ACTION)
                    .putParcelableArrayListExtra(ScanResult.class.getName(), new ArrayList<ScanResult>(results))
                    .putExtra(STATUS_I, STATUS.BATCH)
            );

        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "onScanFailed: "+errorCode);
            mLocalBroadcastManager.sendBroadcast(new Intent()
                    .setAction(ACTION)
                    .putExtra(STATUS_I, -STATUS.ERROR)
            );
        }
    };
}
