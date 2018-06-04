package com.icarbonx.smartdevice.common;

import android.content.Context;

import com.icarbonx.smartdevice.exceptin.BleNotFoundException;
import com.icarbonx.smartdevice.exceptin.BleNotSupportException;
import com.icarbonx.smartdevice.exceptin.NotActivityException;

/**
 * Manager class using {@link android.content.Context}.
 *
 * @author lavi
 */
public abstract class ContextManager {
    protected Context mContext;

//    /**
//     * Constructor.
//     *
//     * @param context {@code context} is a {@link Context} object.
//     */
//    public ContextManager(Context context) {
//        this.mContext = context;
//    }

    /**
     * Make default constructor protected.
     */
    public ContextManager() {
    }

    /**
     * Do initial works.
     * @throws {@link ICarbonXEception} if exception occures.
     */
    public void init(Context context)throws ICarbonXEception{
        this.mContext = context;
    }
}
