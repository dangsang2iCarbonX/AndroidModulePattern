package com.icarbonx.smartdevice.exceptin;

import com.icarbonx.smartdevice.common.ICarbonXEception;

/**
 * Exception class for not activity exception while using activity methods.
 */
public class NotActivityException extends ICarbonXEception {
    public NotActivityException(){
        super("Invoke mthod of activity from not activity object.");
    }
}
