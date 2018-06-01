package com.icarbonx.smartdevice.ble.manager;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.icarbonx.smartdevice.common.PermissionRequestCode;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/**
 * Manage ble scan start/stop and filter devices while scanning
 *
 * @author lavi
 */
public class BleScanManager {
    private static BleScanManager mBleManager;

    //Permission request code
    public static final int REQUEST_PERMISSION_BLE = 0x20;
    //Scan callback
    private IBleScanResult mIBleScanResult;
    //Filters used for scanning
    private List<ScanFilter> mFilters = new ArrayList<>();
    //Settings used for scanning
    private ScanSettings mScanSettings;
    private int mFilterRssi = -200;

    /**
     * Bluetooth LE device scan callbacks. Scan results are reported using these callbacks.
     */
    public interface IBleScanResult {
        /**
         * Callback when a BLE advertisement has been found.
         *
         * @param bleScanDevice {@link BleScanDevice} object
         */
        void onResult(BleScanDevice bleScanDevice);

        /**
         * Fail to start scan
         */
        void onFail();
    }

    /**
     * Get the instance of BleScanManager
     *
     * @param context Context
     * @return BleScanManager object
     */
    public static BleScanManager getInstance(Context context) {
        if (mBleManager == null) {
            mBleManager = new BleScanManager();
            checkPermissions(context);
            checkBleOn(context);
        }
        return mBleManager;
    }

    /**
     * Constructor
     */
    protected BleScanManager() {
        mScanSettings = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0)
                .setUseHardwareBatchingIfSupported(false).build();
    }

    /**
     * Check if bluetooth is not open, or ask to open it.
     */
    private static void checkBleOn(Context context) {
        //判断是否支持蓝牙4.0
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "Bluetooth not found", Toast.LENGTH_SHORT).show();
            if (context instanceof Activity) {
                ((Activity) context).finish();
                //throw exception model

            }
        }
        //获取蓝牙适配器
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        //判断是否支持蓝牙
        if (bluetoothAdapter == null) {
            //不支持
            Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        } else {
            //打开蓝牙
            if (!bluetoothAdapter.isEnabled()) {//判断是否已经打开
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivity(enableBtIntent);
            }
        }
    }

    /**
     * Request needed permissions.
     */
    private static void checkPermissions(Context context) {
        //If build version is less than 23, request permission in AndroidManefest.xml.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH
        };
        ArrayList<String> needPermissions = new ArrayList<>();
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(p);
            }
        }
        //If not all permission needed granted, grant them.
        if (needPermissions.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context, (String[]) needPermissions.toArray(), PermissionRequestCode.BLE_PERMMISION_REQUEST);
        }
    }

    /**
     * Set scan result callback
     *
     * @param iBleScanResult {@link IBleScanResult}
     */
    public void setIBleScanResult(IBleScanResult iBleScanResult) {
        this.mIBleScanResult = iBleScanResult;
    }

    /**
     * Start scanning for devices
     */
    public void startScan() {
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        try {
            scanner.startScan(mFilters, mScanSettings, scanCallback);
        } catch (IllegalArgumentException e) {
            mIBleScanResult.onFail();
        }
    }

    /**
     * Stop scanning for devices
     */
    public void stopScan() {
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        scanner.stopScan(scanCallback);
    }

    /**
     * Release resources
     */
    public void release() {
        mFilters.clear();
        mFilters = null;
        mScanSettings = null;
        mBleManager = null;
    }

    /**
     * Filter scan result by name
     *
     * @param name device name
     */
    public void filterByName(String name) {
        mFilters.add(new ScanFilter.Builder().setDeviceName(name).build());
    }

    /**
     * Filter scan result by device mac address
     *
     * @param mac device name
     */
    public void filterByMac(String mac) {
        mFilters.add(new ScanFilter.Builder().setDeviceAddress(mac).build());
    }

    public void filterByRssi(int rssi) {
        this.mFilterRssi = rssi;
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getRssi() < mFilterRssi) {
                return;
            }
            //callback result
            mIBleScanResult.onResult(new BleScanDevice.Builder().fromResult(result).build());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {

        }

        @Override
        public void onScanFailed(int errorCode) {
            mIBleScanResult.onFail();
        }
    };

}
