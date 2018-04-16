/**
 * @Title: LakalaVoucherRes.java
 * @Package com.cupdata.pointpay.wx.vo.lakala
 * @author LinYong
 * @date 2017年11月15日 下午4:22:43
 * @version V1.0
 */
package com.cupdata.sip.common.api.voucher.response;

import java.util.List;

/**
 * @author LinYong
 * @ClassName: LakalaVoucherRes
 * @Description: 拉卡拉发券接口响应参数
 * @date 2017年11月15日 下午4:22:43
 */
public class LakalaVoucherRes {
    /**
     * 响应结果
     * 成功：true或者失败：false
     */
    private Boolean res;

    /**
     * 券码数据
     */
    private Data data;

    /**
     * 响应信息
     */
    private Message message;

    public Boolean getRes() {
        return res;
    }

    public void setRes(Boolean res) {
        this.res = res;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @author LinYong
     * @ClassName: Data
     * @Description: 券码数据
     * @date 2017年11月15日 下午4:32:53
     */
    public static class Data {
        /**
         * 订单号
         */
        private String order_id;

        /**
         * 券码列表
         */
        private List<Voucher> voucherList;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public List<Voucher> getVoucherList() {
            return voucherList;
        }

        public void setVoucherList(List<Voucher> voucherList) {
            this.voucherList = voucherList;
        }
    }

    /**
     * @author LinYong
     * @ClassName: Voucher
     * @Description: 拉卡拉券码信息
     * @date 2017年11月15日 下午4:28:49
     */
    public static class Voucher {
        /**
         * 券码号
         */
        private String voucher_num;

        /**
         * 价格
         */
        private String price;

        /**
         * 卡密
         */
        private String voucher_pass;

        /**
         * 二维码链接
         */
        private String url;

        /**
         * 券码有效期
         * 格式：yyyy-mm-dd
         */
        private String end_time;

        public String getVoucher_num() {
            return voucher_num;
        }

        public void setVoucher_num(String voucher_num) {
            this.voucher_num = voucher_num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getVoucher_pass() {
            return voucher_pass;
        }

        public void setVoucher_pass(String voucher_pass) {
            this.voucher_pass = voucher_pass;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * @author LinYong
     * @ClassName: Message
     * @Description: 响应信息
     * @date 2017年11月15日 下午4:29:52
     */
    public static class Message {
        /**
         * 响应码
         */
        private String code;

        /**
         * 响应信息描述
         */
        private String mes;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMes() {
            return mes;
        }

        public void setMes(String mes) {
            this.mes = mes;
        }
    }

}
