package com.demo.icarbox.blereceiver;

public class DeviceDataBean {
    private String deviceID;
    private String advData;

    public DeviceDataBean() {
    }
    public DeviceDataBean(String id,String data) {
        this.deviceID=id;
        this.advData=data;
    }

    public String getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
    public String getAdvData() {
        return advData;
    }
    public void setAdvData(String advData) {
        this.advData = advData;
    }
}
