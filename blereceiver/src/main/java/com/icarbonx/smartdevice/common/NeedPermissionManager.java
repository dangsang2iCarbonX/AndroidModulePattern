package com.icarbonx.smartdevice.common;

import android.content.Context;

import com.icarbonx.smartdevice.exceptin.BleNotFoundException;
import com.icarbonx.smartdevice.exceptin.BleNotSupportException;
import com.icarbonx.smartdevice.exceptin.NotActivityException;

/**
 * Manger class that need to request permissions.
 * @author lavi
 */
public abstract class NeedPermissionManager extends ContextManager {

    /**
     * Check and request needed permissions.
     * @throws {@link NotActivityException} if passed context is not {@link android.app.Activity} object.
     */
    protected abstract void checkPermissions() throws NotActivityException;

    @Override
    public void init(Context context)throws ICarbonXEception {
            super.init(context);

            checkPermissions();
    }
}
