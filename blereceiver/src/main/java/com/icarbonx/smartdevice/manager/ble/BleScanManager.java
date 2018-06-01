package com.icarbonx.smartdevice.manager.ble;

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
    private static BleScanManager bleManager;

    //Permission request code
    public static final int REQUEST_PERMISSION_BLE = 0x20;
    //Scan callback
    private IBleScanResult mIBleScanResult ;
    //Filters used for scanning
    private List<ScanFilter> mFilters = new ArrayList<>();
    //Settings used for scanning
    private ScanSettings mScanSettings;
    private int mFilterRssi = -200;

    /**
     * Scan result interface
     */
    public interface IBleScanResult{
        void onResult(BleScanDevice bleScanDevice);
    }

    /**
     * Instance of BleScanManager
     *
     * @param context Context
     * @return BleScanManager object
     */
    public static BleScanManager getInstance(Context context) {
        if (bleManager == null) {
            bleManager = new BleScanManager();
            checkPermissions(context);
            checkBleOn(context);
        }
        return bleManager;
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
     * 检查BLE是否起作用
     */
    private static void checkBleOn(Context context) {
        //判断是否支持蓝牙4.0
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "没有蓝牙设备", Toast.LENGTH_SHORT).show();
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
        //获取蓝牙适配器
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        //判断是否支持蓝牙
        if (bluetoothAdapter == null) {
            //不支持
            Toast.makeText(context, "不支持蓝牙", Toast.LENGTH_SHORT).show();
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
            if (context instanceof Activity) {
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE);
                ActivityCompat.requestPermissions((Activity) context, (String[]) needPermissions.toArray(), REQUEST_PERMISSION_BLE);
            }
        }
    }

    /**
     * Set scan result interface
     *
     * @param iBleScanResult IBleScanResult
     */
    public void setScanResultInterface(IBleScanResult iBleScanResult) {
        this.mIBleScanResult = iBleScanResult;
    }

    /**
     * Start scanning for devices
     */
    public void startScan() {
        BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        scanner.startScan(mFilters, mScanSettings, scanCallback);
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
    public void reset() {
        mFilters.clear();
        mScanSettings = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0)
                .setUseHardwareBatchingIfSupported(false).build();
    }

    /**
     * Filter scan result by name
     * @param name device name
     */
    public void filterByName(String name) {
        mFilters.add(new ScanFilter.Builder().setDeviceName(name).build());
    }

    /**
     * Filter scan result by device mac address
     * @param mac device name
     */
    public void filterByMac(String mac) {
        mFilters.add(new ScanFilter.Builder().setDeviceAddress(mac).build());
    }

    public void filterByRssi(int rssi){
        this.mFilterRssi = rssi;
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getRssi()<mFilterRssi){
                return;
            }
            //callback result
            mIBleScanResult.onResult(new BleScanDevice.Builder().fromResult(result).build());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

}
