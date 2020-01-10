package com.yqbj.yhgy.bean;

import java.util.List;

public class MyLikeBean {

    /**
     * total : 1
     * size : 20
     * current : 1
     * records : [{"accid":"517044816","name":"女神范儿💋💋","gender":2,"headUrl":"https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4Mjk5MDgwOTE3XzNiZDI5MGJmLTE0OGItNDNkNy1hMjNhLWYxNzQxMGQ1MGM0Zg==","birthday":"1993-06-05","height":"173CM","weight":"55KG","job":"1604","cities":"110100","certification":0,"region":"北京市","labeltype":0,"online":1,"vipMember":0,"multimediaSize":0,"config":{"accid":"517044816","hidelocation":0,"hideonline":0,"privacystate":2,"currencyType":2,"viewphotofee":888,"desiredGoals":"关爱我/有趣/看感觉"}}]
     * pages : 1
     */

    private int total;
    private int size;
    private int current;
    private int pages;
    private List<RecordsBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * accid : 517044816
         * name : 女神范儿💋💋
         * gender : 2
         * headUrl : https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4Mjk5MDgwOTE3XzNiZDI5MGJmLTE0OGItNDNkNy1hMjNhLWYxNzQxMGQ1MGM0Zg==
         * birthday : 1993-06-05
         * height : 173CM
         * weight : 55KG
         * job : 1604
         * cities : 110100
         * certification : 0
         * region : 北京市
         * labeltype : 0
         * online : 1
         * vipMember : 0
         * multimediaSize : 0
         * config : {"accid":"517044816","hidelocation":0,"hideonline":0,"privacystate":2,"currencyType":2,"viewphotofee":888,"desiredGoals":"关爱我/有趣/看感觉"}
         */

        private String accid;
        private String name;
        private int gender;
        private String headUrl;
        private String birthday;
        private String height;
        private String weight;
        private String job;
        private String cities;
        private int certification;
        private String region;
        private int labeltype;
        private int online;
        private int distance;
        private int vipMember;
        private int enjoyFlag;
        private int multimediaSize;
        private int blacklistFlag;
        private ConfigBean config;

        public int getBlacklistFlag() {
            return blacklistFlag;
        }

        public void setBlacklistFlag(int blacklistFlag) {
            this.blacklistFlag = blacklistFlag;
        }

        public int getEnjoyFlag() {
            return enjoyFlag;
        }

        public void setEnjoyFlag(int enjoyFlag) {
            this.enjoyFlag = enjoyFlag;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getCities() {
            return cities;
        }

        public void setCities(String cities) {
            this.cities = cities;
        }

        public int getCertification() {
            return certification;
        }

        public void setCertification(int certification) {
            this.certification = certification;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getLabeltype() {
            return labeltype;
        }

        public void setLabeltype(int labeltype) {
            this.labeltype = labeltype;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public int getVipMember() {
            return vipMember;
        }

        public void setVipMember(int vipMember) {
            this.vipMember = vipMember;
        }

        public int getMultimediaSize() {
            return multimediaSize;
        }

        public void setMultimediaSize(int multimediaSize) {
            this.multimediaSize = multimediaSize;
        }

        public ConfigBean getConfig() {
            return config;
        }

        public void setConfig(ConfigBean config) {
            this.config = config;
        }

        public static class ConfigBean {
            /**
             * accid : 517044816
             * hidelocation : 0
             * hideonline : 0
             * privacystate : 2
             * currencyType : 2
             * viewphotofee : 888
             * desiredGoals : 关爱我/有趣/看感觉
             */

            private String accid;
            private int hidelocation;
            private int hideonline;
            private int privacystate;
            private int currencyType;
            private int viewphotofee;
            private String desiredGoals;

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }

            public int getHidelocation() {
                return hidelocation;
            }

            public void setHidelocation(int hidelocation) {
                this.hidelocation = hidelocation;
            }

            public int getHideonline() {
                return hideonline;
            }

            public void setHideonline(int hideonline) {
                this.hideonline = hideonline;
            }

            public int getPrivacystate() {
                return privacystate;
            }

            public void setPrivacystate(int privacystate) {
                this.privacystate = privacystate;
            }

            public int getCurrencyType() {
                return currencyType;
            }

            public void setCurrencyType(int currencyType) {
                this.currencyType = currencyType;
            }

            public int getViewphotofee() {
                return viewphotofee;
            }

            public void setViewphotofee(int viewphotofee) {
                this.viewphotofee = viewphotofee;
            }

            public String getDesiredGoals() {
                return desiredGoals;
            }

            public void setDesiredGoals(String desiredGoals) {
                this.desiredGoals = desiredGoals;
            }
        }
    }
}
