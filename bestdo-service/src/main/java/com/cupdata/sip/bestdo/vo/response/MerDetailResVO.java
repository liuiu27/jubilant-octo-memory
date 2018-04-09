package com.cupdata.sip.bestdo.vo.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tony
 * @date 2018/04/09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MerDetailResVO extends BestaResVO {

    String merItemId;
    String merid;
    String venueNo;
    String venueName;
    String sportType;
    String desc;
    String position;
    String venueMaterial;
    String province;
    String city;
    String district;
    String address;
    String telNumber;
    String onlineTime;
    String images;
    String services;

}
