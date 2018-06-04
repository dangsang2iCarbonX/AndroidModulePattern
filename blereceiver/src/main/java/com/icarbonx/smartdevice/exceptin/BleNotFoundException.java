package com.icarbonx.smartdevice.exceptin;

import com.icarbonx.smartdevice.common.ICarbonXEception;

/**
 * Exception class for BL not found
 * @author lavi
 */
public class BleNotFoundException extends ICarbonXEception {
    public BleNotFoundException(){
        super("Bluetooth not found");
    }
}
