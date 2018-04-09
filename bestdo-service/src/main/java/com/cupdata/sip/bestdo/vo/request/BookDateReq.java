package com.cupdata.sip.bestdo.vo.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tony
 * @date 2018/04/09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookDateReq extends BestdoReqVO {

    String rightProduct;
    String venueNo;
    String sportType;
    String merItemId;
}
