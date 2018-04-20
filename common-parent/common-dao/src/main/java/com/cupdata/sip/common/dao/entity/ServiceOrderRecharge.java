package com.cupdata.sip.common.dao.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "service_order_recharge")
public class ServiceOrderRecharge implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * 主订单ID
     */
    @Column(name = "ORDER_ID")
    private Long orderId;

    /**
     * 服务产品编号
     */
    @Column(name = "PRODUCT_NO")
    private String productNo;

    /**
     * 充值账号
     */
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 开通时长
     */
    @Column(name = "OPEN_DURATION")
    private Long openDuration;

    /**
     * 充值金额 单位：元
     */
    @Column(name = "RECHARGE_AMT")
    private Long rechargeAmt;

    /**
     * 充值流量 仅针对流量充值业务；
     */
    @Column(name = "RECHARGE_TRAFFIC")
    private Long rechargeTraffic;

    /**
     * 充值数量 单位：个，仅在一些特殊服务产品中使用
     */
    @Column(name = "RECHARGE_NUMBER")
    private Long rechargeNumber;

    /**
     * 游戏充值大区
     */
    @Column(name = "GAME_REGION")
    private String gameRegion;

    /**
     * 游戏充值服务器
     */
    @Column(name = "GAME_SERVER")
    private String gameServer;

    /**
     * 创建人
     */
    @Column(name = "CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 修改人
     */
    @Column(name = "UPDATE_BY")
    private String updateBy;

    /**
     * 修改时间
     */
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    private static final long serialVersionUID = 1L;

    /**
     * 获取ID
     *
     * @return ID - ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取主订单ID
     *
     * @return ORDER_ID - 主订单ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置主订单ID
     *
     * @param orderId 主订单ID
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取服务产品编号
     *
     * @return PRODUCT_NO - 服务产品编号
     */
    public String getProductNo() {
        return productNo;
    }

    /**
     * 设置服务产品编号
     *
     * @param productNo 服务产品编号
     */
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    /**
     * 获取充值账号
     *
     * @return ACCOUNT_NUMBER - 充值账号
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * 设置充值账号
     *
     * @param accountNumber 充值账号
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * 获取开通时长
     *
     * @return OPEN_DURATION - 开通时长
     */
    public Long getOpenDuration() {
        return openDuration;
    }

    /**
     * 设置开通时长
     *
     * @param openDuration 开通时长
     */
    public void setOpenDuration(Long openDuration) {
        this.openDuration = openDuration;
    }

    /**
     * 获取充值金额 单位：元
     *
     * @return RECHARGE_AMT - 充值金额 单位：元
     */
    public Long getRechargeAmt() {
        return rechargeAmt;
    }

    /**
     * 设置充值金额 单位：元
     *
     * @param rechargeAmt 充值金额 单位：元
     */
    public void setRechargeAmt(Long rechargeAmt) {
        this.rechargeAmt = rechargeAmt;
    }

    /**
     * 获取充值流量 仅针对流量充值业务；
     *
     * @return RECHARGE_TRAFFIC - 充值流量 仅针对流量充值业务；
     */
    public Long getRechargeTraffic() {
        return rechargeTraffic;
    }

    /**
     * 设置充值流量 仅针对流量充值业务；
     *
     * @param rechargeTraffic 充值流量 仅针对流量充值业务；
     */
    public void setRechargeTraffic(Long rechargeTraffic) {
        this.rechargeTraffic = rechargeTraffic;
    }

    /**
     * 获取充值数量 单位：个，仅在一些特殊服务产品中使用
     *
     * @return RECHARGE_NUMBER - 充值数量 单位：个，仅在一些特殊服务产品中使用
     */
    public Long getRechargeNumber() {
        return rechargeNumber;
    }

    /**
     * 设置充值数量 单位：个，仅在一些特殊服务产品中使用
     *
     * @param rechargeNumber 充值数量 单位：个，仅在一些特殊服务产品中使用
     */
    public void setRechargeNumber(Long rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    /**
     * 获取游戏充值大区
     *
     * @return GAME_REGION - 游戏充值大区
     */
    public String getGameRegion() {
        return gameRegion;
    }

    /**
     * 设置游戏充值大区
     *
     * @param gameRegion 游戏充值大区
     */
    public void setGameRegion(String gameRegion) {
        this.gameRegion = gameRegion;
    }

    /**
     * 获取游戏充值服务器
     *
     * @return GAME_SERVER - 游戏充值服务器
     */
    public String getGameServer() {
        return gameServer;
    }

    /**
     * 设置游戏充值服务器
     *
     * @param gameServer 游戏充值服务器
     */
    public void setGameServer(String gameServer) {
        this.gameServer = gameServer;
    }

    /**
     * 获取创建人
     *
     * @return CREATE_BY - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_DATE - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取修改人
     *
     * @return UPDATE_BY - 修改人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置修改人
     *
     * @param updateBy 修改人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取修改时间
     *
     * @return UPDATE_DATE - 修改时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置修改时间
     *
     * @param updateDate 修改时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}