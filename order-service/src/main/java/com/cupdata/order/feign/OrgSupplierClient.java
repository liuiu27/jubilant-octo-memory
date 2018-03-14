package com.cupdata.order.feign;

import com.cupdata.commons.api.orgsupplier.ISupplierController;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Author: DingCong
 * @Description: 供应商信息获取rest端
 * @CreateDate: 2018/3/13 19:44
 */
@FeignClient(name = "orgSupplier-service")
public interface OrgSupplierClient extends ISupplierController{
}
