package com.icarbonx.smartdevice.common;

/**
 * Listen for permission granted.
 *
 * @author lavi
 */
public interface PermissionListener {
    /**
     * Permission granted
     */
    void permisssionGranted(int requestCode);

    /**
     * Check and request permission wanted.
     */
    void checkPermission();
}
