package com.icarbonx.smartdevice.common;

/**
 * Ble data parser class
 */
public interface IBleParser {
    /**
     * Parse the received <code>data</code>
     *
     * @param data Byte Array
     */
    void parse(byte[] data);
}
