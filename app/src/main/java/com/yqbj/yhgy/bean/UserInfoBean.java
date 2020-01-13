package com.yqbj.yhgy.bean;

import java.io.Serializable;
import java.util.List;

public class UserInfoBean implements Serializable {

    /**
     * wallet : {"currency":0,"money":0,"unassignableTotalCurrency":0,"unassignableTotalMoney":0}
     * contactInfo : {"accid":"144742596","hidecontactinfo":0,"qq":"291252192"}
     * photoAlbum : [{"id":1,"type":1,"statusFlag":0,"payFlag":0,"url":"https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4MzA4ODY4MDkzX2Q2ZDAzNWZlLTc5ZWEtNDdhMC1iNzA5LWE0YzQ2OTQ2YWIwYw==","checkFlag":1}]
     * userDetails : {"accid":"144742596","name":"😂 😚 😌 xgv","gender":1,"mobile":"17610860215","headUrl":"https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4MzA4ODY4MDkzX2Q2ZDAzNWZlLTc5ZWEtNDdhMC1iNzA5LWE0YzQ2OTQ2YWIwYw==","birthday":"2003-03-17","height":"239CM","weight":"119KG","job":"1002","cities":"140800,110100","certification":0,"labeltype":0,"vipMember":1}
     * config : {"accid":"144742596","hidelocation":0,"hideonline":0,"privacystate":1,"desiredGoals":"看感觉/关爱我/有趣/看脸"}
     */

    private WalletBean wallet;
    private ContactInfoBean contactInfo;
    private UserDetailsBean userDetails;
    private ConfigBean config;
    private List<PhotoAlbumBean> photoAlbum;

    public WalletBean getWallet() {
        return wallet;
    }

    public void setWallet(WalletBean wallet) {
        this.wallet = wallet;
    }

    public ContactInfoBean getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoBean contactInfo) {
        this.contactInfo = contactInfo;
    }

    public UserDetailsBean getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsBean userDetails) {
        this.userDetails = userDetails;
    }

    public ConfigBean getConfig() {
        return config;
    }

    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    public List<PhotoAlbumBean> getPhotoAlbum() {
        return photoAlbum;
    }

    public void setPhotoAlbum(List<PhotoAlbumBean> photoAlbum) {
        this.photoAlbum = photoAlbum;
    }

    public static class WalletBean implements Serializable{
        /**
         * currency : 0
         * money : 0.0
         * unassignableTotalCurrency : 0
         * unassignableTotalMoney : 0.0
         */

        private int currency;
        private String money;
        private int unassignableTotalCurrency;
        private String unassignableTotalMoney;

        public int getCurrency() {
            return currency;
        }

        public void setCurrency(int currency) {
            this.currency = currency;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getUnassignableTotalCurrency() {
            return unassignableTotalCurrency;
        }

        public void setUnassignableTotalCurrency(int unassignableTotalCurrency) {
            this.unassignableTotalCurrency = unassignableTotalCurrency;
        }

        public String getUnassignableTotalMoney() {
            return unassignableTotalMoney;
        }

        public void setUnassignableTotalMoney(String unassignableTotalMoney) {
            this.unassignableTotalMoney = unassignableTotalMoney;
        }
    }

    public static class ContactInfoBean implements Serializable {
        /**
         * accid : 144742596
         * hidecontactinfo : 0
         * qq : 291252192
         */

        private String accid;
        private int hidecontactinfo;
        private String qq;
        private String WeChat;

        public String getWeChat() {
            return WeChat;
        }

        public void setWeChat(String weChat) {
            WeChat = weChat;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public int getHidecontactinfo() {
            return hidecontactinfo;
        }

        public void setHidecontactinfo(int hidecontactinfo) {
            this.hidecontactinfo = hidecontactinfo;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }

    public static class UserDetailsBean implements Serializable {
        /**
         * accid : 144742596
         * name : 😂 😚 😌 xgv
         * gender : 1
         * mobile : 17610860215
         * headUrl : https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4MzA4ODY4MDkzX2Q2ZDAzNWZlLTc5ZWEtNDdhMC1iNzA5LWE0YzQ2OTQ2YWIwYw==
         * birthday : 2003-03-17
         * height : 239CM
         * weight : 119KG
         * job : 1002
         * cities : 140800,110100
         * certification : 0
         * labeltype : 0
         * vipMember : 1
         */

        private String accid;
        private String name;
        private int gender;
        private String mobile;
        private String headUrl;
        private String birthday;
        private String height;
        private String weight;
        private String job;
        private String cities;
        private String loginTime;
        private String description;
        private String region;
        private int certification;
        private int labeltype;
        private int vipMember;
        private int online;
        private int distance;
        private int enjoyFlag;
        private int blacklistFlag;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getEnjoyFlag() {
            return enjoyFlag;
        }

        public void setEnjoyFlag(int enjoyFlag) {
            this.enjoyFlag = enjoyFlag;
        }

        public int getBlacklistFlag() {
            return blacklistFlag;
        }

        public void setBlacklistFlag(int blacklistFlag) {
            this.blacklistFlag = blacklistFlag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public int getLabeltype() {
            return labeltype;
        }

        public void setLabeltype(int labeltype) {
            this.labeltype = labeltype;
        }

        public int getVipMember() {
            return vipMember;
        }

        public void setVipMember(int vipMember) {
            this.vipMember = vipMember;
        }
    }

    public static class ConfigBean implements Serializable {
        /**
         * accid : 144742596
         * hidelocation : 0
         * hideonline : 0
         * privacystate : 1
         * desiredGoals : 看感觉/关爱我/有趣/看脸
         */

        private String accid;
        private int hidelocation;
        private int hideonline;
        private int invisible;
        private int privacystate;
        private int currencyType;
        private int viewphotofee;
        private String desiredGoals;
        private String datingPrograms;

        public int getInvisible() {
            return invisible;
        }

        public void setInvisible(int invisible) {
            this.invisible = invisible;
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

        public String getDatingPrograms() {
            return datingPrograms;
        }

        public void setDatingPrograms(String datingPrograms) {
            this.datingPrograms = datingPrograms;
        }

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

        public String getDesiredGoals() {
            return desiredGoals;
        }

        public void setDesiredGoals(String desiredGoals) {
            this.desiredGoals = desiredGoals;
        }
    }

    public static class PhotoAlbumBean implements Serializable {
        /**
         * id : 1
         * type : 1
         * statusFlag : 0
         * payFlag : 0
         * url : https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4MzA4ODY4MDkzX2Q2ZDAzNWZlLTc5ZWEtNDdhMC1iNzA5LWE0YzQ2OTQ2YWIwYw==
         * checkFlag : 1
         */

        private int id;
        private int type;
        private int statusFlag;
        private int payFlag;
        private String url;
        private int checkFlag;
        private int currencyType;
        private int fee;
        private int selfFlag;
        private int paidFlag;
        private int setOnFire;

        public int getPaidFlag() {
            return paidFlag;
        }

        public void setPaidFlag(int paidFlag) {
            this.paidFlag = paidFlag;
        }

        public int getSetOnFire() {
            return setOnFire;
        }

        public void setSetOnFire(int setOnFire) {
            this.setOnFire = setOnFire;
        }

        public int getSelfFlag() {
            return selfFlag;
        }

        public void setSelfFlag(int selfFlag) {
            this.selfFlag = selfFlag;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getCurrencyType() {
            return currencyType;
        }

        public void setCurrencyType(int currencyType) {
            this.currencyType = currencyType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatusFlag() {
            return statusFlag;
        }

        public void setStatusFlag(int statusFlag) {
            this.statusFlag = statusFlag;
        }

        public int getPayFlag() {
            return payFlag;
        }

        public void setPayFlag(int payFlag) {
            this.payFlag = payFlag;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getCheckFlag() {
            return checkFlag;
        }

        public void setCheckFlag(int checkFlag) {
            this.checkFlag = checkFlag;
        }
    }
}
