package com.cupdata.commons.api.voucher;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.orgsupplier.OrgInfVo;
import com.cupdata.commons.vo.voucher.GetVoucherRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auth: LinYong
 * @Description:券码服务接口
 * @Date: 13:06 2017/12/14
 */
public interface IVoucherController {
    /**
     * 根据机构编号，查询机构信息
     * @return
     */
    @GetMapping("/voucher/{productNo}")
    public BaseResponse<GetVoucherRes> createByProductNo(@PathVariable("productNo") String productNo);
}
