package com.icarbonx.smartdevice.ble.manager;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

import no.nordicsemi.android.support.v18.scanner.ScanResult;

/**
 * Bluetooth device class from {@link ScanResult}
 */
public class BleScanDevice implements Parcelable{
    //Device from scan result
    private ScanResult mScanResult;

    private BleScanDevice(ScanResult scanResult) {
        this.mScanResult = scanResult;
    }

    /**
     * Get scanned device's name
     *
     * @return Device's name
     * @throws NullPointerException If {@code ScanResult} is null of the method{@link Builder#fromResult(ScanResult)}.
     */
    public String getName() throws NullPointerException{
        if (mScanResult==null)throw new NullPointerException("You should build from a not null ScanResult object");

        //Priority get device name
        if (mScanResult.getDevice().getName() != null) {
            return mScanResult.getDevice().getName();
        }
        //Get advertised device local name
        if (mScanResult.getScanRecord().getDeviceName() != null) {
            return mScanResult.getScanRecord().getDeviceName();
        }
        //Got no device name return specified String as name
        return String.format("iCarbonX-200");
    }

    /**
     * Get scanned device's MAC
     *
     * @return Device's MAC
     */
    public String getMac(){

        return mScanResult.getDevice().getAddress();
    }

    /**
     * Get scanned device's RSSI
     *
     * @return Device's RSSI when scanned
     */
    public int getRssi() {

        return mScanResult.getRssi();
    }

    /**
     * Get the device's advertised raw data
     *
     * @return Returns raw bytes of scan record.
     */
    public byte[] getScanRawData() {

        return mScanResult.getScanRecord().getBytes();
    }

    /**
     * Get the specified service data advetised by device.
     *
     * @return Returns the service data byte array associated with the {@code serviceUuid}. Returns
     * {@code null} if the {@code serviceDataUuid} is not found.
     */
    public byte[] getScanDataByService(ParcelUuid serviceDataUuid){

        if (serviceDataUuid == null) return null;
        return mScanResult.getScanRecord().getServiceData(serviceDataUuid);
    }

    /**
     * Compare by mac of device. If not same object, always true.
     *
     * @param obj {@link BleScanDevice}
     * @return True of same mac,or false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BleScanDevice) {
            return getMac().equals(((BleScanDevice) obj).getMac());
        }
        return false;
    }

    @Override
    public String toString() {
        if(mScanResult!=null){
            return mScanResult.toString();
        }

        return "empty";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private BleScanDevice(Parcel in) {
        readFromParcel(in);
    }
    private void readFromParcel(Parcel in) {
        if (in.readInt() == 1) {
            this.mScanResult = ScanResult.CREATOR.createFromParcel(in);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(this.mScanResult!=null){
            dest.writeInt(1);
            this.mScanResult.writeToParcel(dest,flags);
        }else {
            dest.writeInt(0);
        }
    }

    public static final Parcelable.Creator<BleScanDevice> CREATOR = new Creator<BleScanDevice>() {
        @Override
        public BleScanDevice createFromParcel(Parcel source) {
            return new BleScanDevice(source);
        }

        @Override
        public BleScanDevice[] newArray(int size) {
            return new BleScanDevice[size];
        }
    };

    /**
     * Builder class for {@link BleScanDevice}.
     */
    public static final class Builder{
        private ScanResult mScanResult;

        /**
         * Save {@link ScanResult}
         * @param scanResult The {@link ScanResult}
         */
        public Builder fromResult(ScanResult scanResult){
            this.mScanResult = scanResult;
            return this;
        }

        /**
         * Build {@link BleScanDevice}.
         */
        public BleScanDevice build(){
            return new BleScanDevice(mScanResult);
        }
    }
}
