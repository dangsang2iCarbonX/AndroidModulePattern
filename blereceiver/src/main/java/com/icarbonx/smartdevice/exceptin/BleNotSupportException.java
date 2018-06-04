package com.icarbonx.smartdevice.exceptin;

import com.icarbonx.smartdevice.common.ICarbonXEception;

/**
 * Exception class for BL not support feature
 * @author lavi
 */
public class BleNotSupportException extends ICarbonXEception {
    public BleNotSupportException(){
        super("Bluetooth not found");
    }
}
