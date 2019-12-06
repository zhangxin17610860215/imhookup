package com.netease.nim.uikit.common.util;

import java.util.List;

public class CityBean {

    /**
     * text : 北京市
     * value : 110000
     * child : [{"text":"北京市","value":"110100","child":[{"text":"东城区","value":"110101"},{"text":"西城区","value":"110102"},{"text":"朝阳区","value":"110105"},{"text":"丰台区","value":"110106"},{"text":"石景山区","value":"110107"},{"text":"海淀区","value":"110108"},{"text":"门头沟区","value":"110109"},{"text":"房山区","value":"110111"},{"text":"通州区","value":"110112"},{"text":"顺义区","value":"110113"},{"text":"昌平区","value":"110114"},{"text":"大兴区","value":"110115"},{"text":"怀柔区","value":"110116"},{"text":"平谷区","value":"110117"},{"text":"密云区","value":"110118"},{"text":"延庆区","value":"110119"}]}]
     */

    private String text;
    private String value;
    private List<ChildBeanX> child;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ChildBeanX> getChild() {
        return child;
    }

    public void setChild(List<ChildBeanX> child) {
        this.child = child;
    }

    public static class ChildBeanX {
        /**
         * text : 北京市
         * value : 110100
         * child : [{"text":"东城区","value":"110101"},{"text":"西城区","value":"110102"},{"text":"朝阳区","value":"110105"},{"text":"丰台区","value":"110106"},{"text":"石景山区","value":"110107"},{"text":"海淀区","value":"110108"},{"text":"门头沟区","value":"110109"},{"text":"房山区","value":"110111"},{"text":"通州区","value":"110112"},{"text":"顺义区","value":"110113"},{"text":"昌平区","value":"110114"},{"text":"大兴区","value":"110115"},{"text":"怀柔区","value":"110116"},{"text":"平谷区","value":"110117"},{"text":"密云区","value":"110118"},{"text":"延庆区","value":"110119"}]
         */

        private String text;
        private String value;
        private List<ChildBean> child;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean {
            /**
             * text : 东城区
             * value : 110101
             */

            private String text;
            private String value;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
