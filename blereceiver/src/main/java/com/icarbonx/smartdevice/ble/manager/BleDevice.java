package com.icarbonx.smartdevice.ble.manager;

/**
 * Bluetooth device class
 *
 * @author lavi
 */
public class BleDevice {
    //Device name
    String name = "";
    //Device MAC address
    String mac = "";
    //Device current RSSI
    int rssi = -200;
    //Device current scan data
    byte[] scanData;


    /**
     * Private class constructor
     */
    public BleDevice(String name, String mac, int rssi, byte[] data) {
        this.name = name;
        this.mac = mac;
        this.rssi = rssi;
        this.scanData = data;
    }

    /**
     * Default constructor
     */
    public BleDevice() {
    }

    /**
     * Initialize class from {@link BleScanDevice}
     *
     * @param scanDevice {@link BleScanDevice} object
     */
    public void initFromScanResult(BleScanDevice scanDevice) {
        if (scanDevice == null) return;

        this.name = scanDevice.getName();

        this.rssi = scanDevice.getRssi();
        this.mac = scanDevice.getMac();
        this.scanData = scanDevice.getScanRawData();
    }

    /**
     * Get device's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set device name
     *
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get device's MAC address
     */
    public String getMac() {
        return mac;
    }

    /**
     * Set device's MAC
     *
     * @param mac device's MAC as String
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * Get device's current Rssi
     */
    public int getRssi() {
        return rssi;
    }

    /**
     * Set device Rssi
     *
     * @param rssi Current Rssi
     */
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    /**
     * Get device's scanned data
     */
    public byte[] getScanData() {
        return scanData;
    }

    /**
     * Set device scanned data
     *
     * @param scanData Device's scanned data
     */
    public void setScanData(byte[] scanData) {
        this.scanData = scanData;
    }

    /**
     * Set device name
     *
     * @param name Name
     * @return {@link BleDevice} object
     */
    public BleDevice addName(String name) {
        this.name = name;

        return this;
    }

    /**
     * Set device's MAC
     *
     * @param mac device's MAC as String
     * @return {@link BleDevice} object
     */
    public BleDevice addMac(String mac) {
        this.mac = mac;

        return this;
    }

    /**
     * Set device Rssi
     *
     * @param rssi Current Rssi
     * @return {@link BleDevice} object
     */
    public BleDevice addRssi(int rssi) {
        this.rssi = rssi;

        return this;
    }

    /**
     * Set device scanned data
     *
     * @param scanData Device's scanned data
     * @return {@link BleDevice} object
     */
    public BleDevice addScanData(byte[] scanData) {
        if (scanData == null) return this;

        this.scanData = new byte[scanData.length];
        System.arraycopy(scanData, 0, this.scanData, 0, scanData.length);
        return this;
    }


    /**
     * Compare by mac of device. If not same object, always false.
     *
     * @param obj {@link BleDevice} object
     * @return True if same MAC, or false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BleDevice) {
            return mac.equals(((BleDevice) obj).mac);
        }
        return true;
    }

}
