package com.icarbonx.smartdevice.ble.manager;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.icarbonx.smartdevice.common.ICarbonXEception;
import com.icarbonx.smartdevice.common.NeedPermissionManager;
import com.icarbonx.smartdevice.common.PermissionRequestCode;
import com.icarbonx.smartdevice.exceptin.BleNotFoundException;
import com.icarbonx.smartdevice.exceptin.BleNotSupportException;
import com.icarbonx.smartdevice.exceptin.NotActivityException;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/**
 * Manage ble scan start/stop and filter devices while scanning
 * Work in the caller thread.
 * <p>After call the class method {@link BleScanManager#getInstance()}, should call {@link BleScanManager#init(Context)} to pass {@code context} in.</p>
 *
 * @author lavi
 */
public class BleScanManager extends NeedPermissionManager {
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
     * @return {@link BleScanManager} object
     */
    public static BleScanManager getInstance() {
        if (mBleManager == null) {
            mBleManager = new BleScanManager();
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

    @Override
    public void init(Context context) throws ICarbonXEception {

        checkBleOn();

        super.init(context);
    }

    /**
     * Request needed permissions.
     */
    @Override
    protected void checkPermissions() throws NotActivityException {
        if (mContext==null)return;

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
            if (ContextCompat.checkSelfPermission(mContext, p) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(p);
            }
        }
        //If not all permission needed granted, grant them.
        if (needPermissions.size() > 0) {
            if (mContext instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) mContext, (String[]) needPermissions.toArray(), PermissionRequestCode.BLE_PERMMISION_REQUEST);
            } else {
                throw new NotActivityException();
            }
        }
    }

    /**
     * Check if bluetooth is not open, or ask to open it.
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    private void checkBleOn() throws BleNotSupportException, BleNotFoundException, NotActivityException {
        if(mContext==null)return;

        //判断是否支持蓝牙4.0
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //Toast.makeText(context, "Bluetooth not found", Toast.LENGTH_SHORT).show();
            throw new BleNotFoundException();
//            if (context instanceof Activity)
//            {
//                ((Activity) context).finish();
//            }
        }
        //获取蓝牙适配器
        BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        //判断是否支持蓝牙
        if (bluetoothAdapter == null) {
            //不支持
//            Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
//            if (context instanceof Activity) {
//                ((Activity) context).finish();
//            }
            throw new BleNotSupportException();
        } else {
            //打开蓝牙
            if (!bluetoothAdapter.isEnabled()) {//判断是否已经打开
                if (mContext instanceof Activity) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    ((Activity) mContext).startActivity(enableBtIntent);
                } else {
                    throw new NotActivityException();
                }
            }
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
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
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
    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
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
