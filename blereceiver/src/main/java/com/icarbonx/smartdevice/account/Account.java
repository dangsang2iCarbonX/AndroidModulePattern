package com.icarbonx.smartdevice.account;

/**
 * Base account to decide common proterties
 */
public class Account implements JsonInterface{

    @Override
    public String toJson() {
        return "{}";
    }
}
