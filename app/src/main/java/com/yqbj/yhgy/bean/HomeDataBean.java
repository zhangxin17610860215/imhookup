package com.yqbj.yhgy.bean;

import java.util.List;

public class HomeDataBean {

    /**
     * total : 1
     * size : 20
     * current : 1
     * records : [{"accid":"517044816","name":"女神范儿💋💋","gender":2,"headUrl":"https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4MDQzNTU3MzA1Xzk0Y2IyZGE1LTg0M2QtNGY4Yi04MmVkLWZhMWI0ZGNjMWEyZQ==","birthday":"2020-01-05T16:00:00.000+0000","height":"173CM","weight":"56KG","job":"1002","cities":"510100","certification":0,"labeltype":0,"distance":0}]
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
         * headUrl : https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4MDQzNTU3MzA1Xzk0Y2IyZGE1LTg0M2QtNGY4Yi04MmVkLWZhMWI0ZGNjMWEyZQ==
         * birthday : 2020-01-05T16:00:00.000+0000
         * height : 173CM
         * weight : 56KG
         * job : 1002
         * cities : 510100
         * certification : 0
         * labeltype : 0
         * distance : 0
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
        private String description;
        private int certification;
        private int labeltype;
        private int distance;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public int getLabeltype() {
            return labeltype;
        }

        public void setLabeltype(int labeltype) {
            this.labeltype = labeltype;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }
    }
}
