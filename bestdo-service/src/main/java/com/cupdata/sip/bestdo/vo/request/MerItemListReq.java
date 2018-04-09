package com.cupdata.sip.bestdo.vo.request;

import lombok.Data;

/**
 * @author Tony
 * @date 2018/04/08
 */
@Data
public class MerItemListReq extends BestdoReqVO {

    String rightProduct;

    String rightName;

    String cityMark;

    String sportType;

    String merid;

    String venueName;

    String currentPage ="1";

    String pageSize ="100";

    String rtnFlag;

}
