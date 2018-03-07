package com.cupdata.commons.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: DingCong
 * @Description: 券码类型表
 * @CreateDate: 2018/2/27 19:44
 */
@Data
public class ElectronicVoucherCategory extends BaseModel{

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
    }
