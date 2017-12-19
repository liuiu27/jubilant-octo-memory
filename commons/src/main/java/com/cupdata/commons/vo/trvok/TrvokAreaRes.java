package com.cupdata.commons.vo.trvok;

import com.cupdata.commons.vo.BaseData;
import lombok.Data;

import java.util.List;

/**
 * @Auth: LinYong
 * @Description:空港易行获取区域信息接口响应参数
 * @Date: 14:47 2017/12/19
 */
@Data
public class TrvokAreaRes extends BaseData {
    /**
     * 机场列表
     */
    private List<AirportSummary> airportList;

    @Data
    public static class AirportSummary {
        /**
         *机场编号
         */
        private String airportId;

        /**
         * 机场名称
         */
        private String airportName;

        /**
         * 机场三字简码
         */
        private String airportCode;
    }
}
