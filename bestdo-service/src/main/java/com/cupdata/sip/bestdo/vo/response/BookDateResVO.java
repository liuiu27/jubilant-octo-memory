package com.cupdata.sip.bestdo.vo.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Tony
 * @date 2018/04/09
 */

@Getter
@Setter
public class BookDateResVO extends BestaResVO<List<BookDateResVO.BookDateRes>> {

    Integer timeType;

    String venueNo;

    @Data
    static class BookDateRes{

        String day;

        String inventoryInfo;

        String pieceId;

        String pieceName;

        String startHour;

    }
}
