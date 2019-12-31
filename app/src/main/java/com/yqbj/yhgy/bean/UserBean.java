package com.yqbj.yhgy.bean;

public class UserBean {

    /**
     * info : {"accid":"630256543","account":"13453993655","nikename":"😂 😚 😌 xgv","yunxinToken":"f720be875e4fcd3ea4d4e8dd6de427dd","userToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJcdUQ4M0RcdURFMDIgXHVEODNEXHVERTFBIFx1RDgzRFx1REUwQyB4Z3YiLCJuYW1lIjoiXHVEODNEXHVERTAyIFx1RDgzRFx1REUxQSBcdUQ4M0RcdURFMEMgeGd2IiwibW9iaWxlIjoiMTM0NTM5OTM2NTUiLCJpc3MiOiJzZC1sb2dpbi1zZXJ2ZXIiLCJhY2NpZCI6IjYzMDI1NjU0MyIsImV4cCI6MTU3NzcwODkyMSwiaWF0IjoxNTc3NzAxNzIxfQ.NEhlFp76NZWs2lXramz-SNev3-5p7lqU7X2Drk3UKVl90l1zpuCP9cuSpWW-yGKjlQDZw-0Qi92PW0MVt8VZTw","serverDomain":"192.168.1.112:8085"}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * accid : 630256543
         * account : 13453993655
         * nikename : 😂 😚 😌 xgv
         * yunxinToken : f720be875e4fcd3ea4d4e8dd6de427dd
         * userToken : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJcdUQ4M0RcdURFMDIgXHVEODNEXHVERTFBIFx1RDgzRFx1REUwQyB4Z3YiLCJuYW1lIjoiXHVEODNEXHVERTAyIFx1RDgzRFx1REUxQSBcdUQ4M0RcdURFMEMgeGd2IiwibW9iaWxlIjoiMTM0NTM5OTM2NTUiLCJpc3MiOiJzZC1sb2dpbi1zZXJ2ZXIiLCJhY2NpZCI6IjYzMDI1NjU0MyIsImV4cCI6MTU3NzcwODkyMSwiaWF0IjoxNTc3NzAxNzIxfQ.NEhlFp76NZWs2lXramz-SNev3-5p7lqU7X2Drk3UKVl90l1zpuCP9cuSpWW-yGKjlQDZw-0Qi92PW0MVt8VZTw
         * serverDomain : 192.168.1.112:8085
         */

        private String accid;
        private String account;
        private String nikename;
        private String yunxinToken;
        private String userToken;
        private String serverDomain;

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getYunxinToken() {
            return yunxinToken;
        }

        public void setYunxinToken(String yunxinToken) {
            this.yunxinToken = yunxinToken;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public String getServerDomain() {
            return serverDomain;
        }

        public void setServerDomain(String serverDomain) {
            this.serverDomain = serverDomain;
        }
    }
}
