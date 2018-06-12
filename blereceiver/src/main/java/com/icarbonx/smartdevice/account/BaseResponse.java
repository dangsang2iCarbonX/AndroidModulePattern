package com.icarbonx.smartdevice.account;

import com.google.gson.Gson;

/**
 * Base response class for user account controller
 * @author lavi
 */
public class BaseResponse<T> implements JsonInterface{
    T data;

    //error message
    String errMsg;
    //error code
    int errorCode;
    //current pasge index
    int pageIndex;
    //current page size
    int pageSize;
    //time stamp for the response
    long timestamp;
    //total number
    int totalNum;
    //total page count
    int totalPage;

    public T getData() {
        return data;
    }

    public BaseResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    @Override
    public String toString() {
        return toJson();
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
