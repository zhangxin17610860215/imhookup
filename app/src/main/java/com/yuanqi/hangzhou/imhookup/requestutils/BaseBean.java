package com.yuanqi.hangzhou.imhookup.requestutils;

public class BaseBean {

    /**
     * statusCode : 200
     * message : success
     * data : {"apiToken":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJUb2tlblR5cGVcIjpcInRva2VuX2FwaVwifSIsIlRva2VuVHlwZSI6InRva2VuX2FwaSIsImlzcyI6ImltX3NlcnZlciIsImV4cCI6MTU1NDcyMDg4MiwiaWF0IjoxNTU0NzIwODIyLCJqdGkiOiJ4aWFsaWFvX3YxIn0.DG-Hh7xKdINFy0SXDYvnNtR-xGA9MF2thXhkMXCRJVk","domain":"139.196.106.67","key":"GMR11fH3t3x84lmvai8BPNBMFqm+sFtRBXS9IqM7j6aYnBSfSN3gvTfCVW3AgiDktcAnkWi6Qj6CdqIZjpO1ggHzbLWTQt1j+HtvCXrv5e9W8wzSg87WDOpv7pSOFQMx/er3txzIDJD564fLSd3vP19blnRpVMJNYlAhxV0Am9g="}
     */

    private int statusCode;
    private String message;
    private String data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
