package com.cupdata.iyooc.feign;


import com.cupdata.sip.common.api.voucher.ILocalVoucherController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "voucher-service")
public interface VoucherFeignClient extends ILocalVoucherController {
}
