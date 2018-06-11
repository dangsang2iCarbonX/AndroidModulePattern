package com.icarbonx.smartdevice.account;

import com.google.gson.Gson;

/**
 * User account information
 * @author lavi
 */
public class UserAccount extends Account{
    //account id
    long accountId;
    //account name
    String accountName;
    //account email
    String email;
    //if nor has receive address
    boolean hasReceiveAddress;
    //phone number
    String phoneNumber;
    //photo url
    String photoUrl;
    //receive address id
    int receiveAddressId;
    //gender,1 male,0 female
    int sex;

    @Override
    public String toJson() {
        return new Gson().toJson(this,UserAccount.class);
    }


    public long getAccountId() {
        return accountId;
    }

    public UserAccount setAccountId(long accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public UserAccount setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserAccount setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isHasReceiveAddress() {
        return hasReceiveAddress;
    }

    public UserAccount setHasReceiveAddress(boolean hasReceiveAddress) {
        this.hasReceiveAddress = hasReceiveAddress;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserAccount setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public UserAccount setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public int getReceiveAddressId() {
        return receiveAddressId;
    }

    public UserAccount setReceiveAddressId(int receiveAddressId) {
        this.receiveAddressId = receiveAddressId;
        return this;
    }

    public int getSex() {
        return sex;
    }

    public UserAccount setSex(int sex) {
        this.sex = sex;
        return this;
    }

    @Override
    public String toString() {
        return toJson();
    }
}
