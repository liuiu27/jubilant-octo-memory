package com.cupdata.commons.vo.trvok;

import com.cupdata.commons.vo.BaseData;
import lombok.Data;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description:空港易行获取机场详细信息接口响应参数
 * @Date: 14:48 2017/12/19
 */
@Data
public class TrvokAirportRes extends BaseData {
    /**
     * 休息室详情列表
     */
    private List<LoungeDetail> airportInfo;

    @Data
    public static class LoungeDetail {
        /**
         *机场编号
         */
        private String airportId;

        /**
         *机场名称
         */
        private String airportName;

        /**
         *航站楼名称
         */
        private String airportTerminal;

        /**
         *出发口
         */
        private String departurePort;

        /**
         *设施
         */
        private String facilities;
        /**
         *营业时间段
         */
        private String hoursSection;
        /**
         *休息室图片地址
         */
        private String image;

        /**
         *休息室图片地址2
         */
        private String image2;
        /**
         *休息室图片地址3
         */
        private String image3;
        /**
         *休息室图片地址4
         */
        private String image4;
        /**
         *休息室图片地址5
         */
        private String image5;

        /**
         *休息室位置
         */
        private String loungeAddress;

        /**
         *休息室描述
         */
        private String loungeDesc;

        /**
         *
         */
        private String popular;

        /**
         *最近登机口
         */
        private String recentlyGate;

        /**
         *区域描述
         */
        private String region;

        /**
         *相关规定
         */
        private String regulations;

        /**
         *休息室名称
         */
        private String areaName;

        /**
         * 状态
         * 0：不可使用；1：可使用；
         */
        private String enabled;

        /**
         * 休息室类型
         * 0：国内；1：国际
         */
        private String international;

        /**
         * 服务类型
         */
        private String services;
    }
}
