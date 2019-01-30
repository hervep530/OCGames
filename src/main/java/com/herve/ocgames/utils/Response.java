package com.herve.ocgames.utils;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private int errCode;
    private String errSummary;
    private String errMessage;
    private boolean success;
    private String message;
    private Map<String, Object> values;

    /**
     *
     * Use Diamond Type introduce with java 1.7
     */
    public Response() {
        this.errCode = 0;
        this.errSummary = "";
        this.errMessage = "";
        this.success = true;
        this.message = "";
        // this.values = null;
        this.values = new HashMap<>();
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int code) {
        this.errCode = code;
    }

    public String getErrSummary() {
        return errSummary;
    }

    public void setErrSummary(String summary) {
        this.errSummary = summary;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String details) {
        this.errMessage = details;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean aTrue) {
        success = aTrue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getValue(String key) {
        if (this.values.containsKey(key))
            return this.values.get(key);
        else
            return null;
    }

    public void appendValue(String key, Object object) {
        this.values.put(key, object);
    }

    public void deleteValue(String key){
        this.values.remove(key);
    }

}
