package com.demo.icarbox.blereceiver;

import java.util.Arrays;

/**
 * Bluetooth device class
 */
public class BleDevice {
    //Device name
    String name="";
    //Device MAC address
    String mac="";
    //Device current RSSI
    int rssi=-51;
    //Device current scan data
    byte[] scanData;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getScanData() {
        return scanData;
    }

    public void setScanData(byte[] scanData) {
        this.scanData = scanData;
    }

    public BleDevice Name(String name){
        this.name = name;
        return this;
    }

    public BleDevice Mac(String mac){
        this.mac = mac;
        return this;
    }

    public BleDevice Rssi(int rssi){
        this.rssi = rssi;
        return this;
    }
    public BleDevice ScanData(byte[] scanData){
        this.scanData = scanData;
        return this;
    }

    /**
     * Compare by mac of device. If not same object, always true.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BleDevice){
            return mac.equals(((BleDevice) obj).mac);
        }
        return true;
    }
}
