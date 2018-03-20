package com.cupdata.iqiyi.vo;

/**
 * @Author: DingCong
 * @Description: 爱奇艺充值请求vo
 * @CreateDate: 2018/2/6 14:52
 */
public class IqiyiRechargeReq {

    /**
     * 爱奇艺账号
     */
    private String userAccount;

    /**
     * 充值激活码
     */
    private String cardCode;

    /**
     * 充值商家商户号
     */
    private String partnerNo;

    /**
     * 签名
     */
    private String sign;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getPartnerNo() {
        return partnerNo;
    }

    public void setPartnerNo(String partnerNo) {
        this.partnerNo = partnerNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
