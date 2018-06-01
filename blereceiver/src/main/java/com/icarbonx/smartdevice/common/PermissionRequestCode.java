package com.icarbonx.smartdevice.common;


/**
 * Every kind of request code for requesting permissions or open/close hardware etc.
 */
public class PermissionRequestCode {
    private static final int BASE_CODE = 0x183;
    //Network request code
    public static final int NETWORK_PERMMISION_REQUEST = BASE_CODE + 0x01;
    //Bluetooth request code
    public static final int BLE_PERMMISION_REQUEST = BASE_CODE + 0x02;

}
