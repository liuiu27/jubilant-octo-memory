package com.cupdata.commons.api.orgsupplier;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.model.ServiceSupplier;

/**
 * @Auth: LinYong
 * @Description:机构、服务供应商服务接口
 * @Date: 13:11 2017/12/14
 */
@RequestMapping("/supplier")
public interface ISupplierController {
	  /**
     * 查询所有供应商信息
     */
    @GetMapping("/selectAll")
    public List<ServiceSupplier> selectAll();

}
