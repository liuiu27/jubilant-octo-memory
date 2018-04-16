package com.cupdata.sip.common.api.bestdo.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author junliang
 * @date 2018/04/13
 */
@Data
public class OrderCreateReqVO {

    @NotBlank
    private String sportType;
    @NotBlank
    private String merid;
    @NotBlank
    private String merItemId;
    @NotBlank
    private String venueNo;
    private String cupdOrderNo;
    @NotBlank
    private String bookDay;
    @NotBlank
    private String bookPhone;

    private String note;
    @NotBlank
    private String rightProduct;

    @NotNull
    @Valid
    private List<OrderDateDetailVO> items;


    @Data
    static class OrderDateDetailVO {

        @NotBlank
        private String playTime;
        @NotBlank
        private String startHour;
        @NotBlank
        private String endHour;

    }

}
