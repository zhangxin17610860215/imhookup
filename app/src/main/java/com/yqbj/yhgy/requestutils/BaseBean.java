package com.yqbj.yhgy.requestutils;

import java.util.Map;

public class BaseBean {

    /**
     * code : 200
     * msg : 操作成功
     * data : {"info":{"accid":"985857181","account":"13453993655","nikename":"13453993655","yunxinToken":"d04942616672b14fb21f99835c18b020","userToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzQ1Mzk5MzY1NSIsIm5hbWUiOiIxMzQ1Mzk5MzY1NSIsIm1vYmlsZSI6IjEzNDUzOTkzNjU1IiwiaXNzIjoic2QtbG9naW4tc2VydmVyIiwiYWNjaWQiOiI5ODU4NTcxODEiLCJleHAiOjE1Nzc3ODcyMDQsImlhdCI6MTU3Nzc4MDAwNH0.YiQ3qdAU82Q5-auAD74mzcKzQSHbJfih0jXoMcs6Pv819gvuNUK4-wEATouMjqEri-5JXx6gQ3Kmq990F7wQYA","serverDomain":"192.168.1.114:8085"}}
     */

    private int code;
    private String msg;
    private Map<String, Object> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
