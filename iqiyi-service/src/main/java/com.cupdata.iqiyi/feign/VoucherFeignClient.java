package com.cupdata.iqiyi.feign;


import com.cupdata.commons.api.voucher.ILocalVoucherController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "voucher-service")
public interface VoucherFeignClient extends ILocalVoucherController {
}
