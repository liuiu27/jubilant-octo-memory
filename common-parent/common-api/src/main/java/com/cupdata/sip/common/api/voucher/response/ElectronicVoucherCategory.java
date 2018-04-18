package com.cupdata.sip.common.api.voucher.response;

import lombok.Data;

import java.util.Date;

/**
 * @Author: DingCong
 * @Description: 券码类型表
 * @CreateDate: 2018/2/27 19:44
 */
@Data
public class ElectronicVoucherCategory {

    /**
     * 主键ID
     */
    protected Long id;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 商户ID
     */
    private Long supplierId;

    /**
     * 券码类别名称
     */
    private String categoryName;

    /**
     * 券码状态
     */
    private Character validStatus;

    /**
     * 库存警告
     */
    private String stockWarning;

    /**
     * 券码类别id
     */
    private Long categoryId;
}
