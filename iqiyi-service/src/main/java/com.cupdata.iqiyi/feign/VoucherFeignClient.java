package com.cupdata.iqiyi.feign;


import com.cupdata.commons.api.voucher.IVoucherApi;
import com.cupdata.commons.api.voucher.IVoucherController;
import com.cupdata.commons.api.voucher.IVoucherGetController;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(name = "voucher-service")
public interface VoucherFeignClient extends IVoucherGetController {
}
