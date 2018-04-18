package com.cupdata.voucher.biz;


import com.cupdata.sip.common.api.voucher.response.ElectronicVoucherCategory;
import com.cupdata.sip.common.api.voucher.response.ElectronicVoucherLib;
import com.cupdata.sip.common.dao.entity.ElecVoucherCategory;
import com.cupdata.sip.common.dao.entity.ElecVoucherLib;
import com.cupdata.sip.common.dao.mapper.ElecVoucherCategoryMapper;
import com.cupdata.sip.common.dao.mapper.ElecVoucherLibMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.VoucherException;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auth: DingCong
 * @Description: 券码相关服务
 * @Date: 20:20 2017/12/14
 */
@Slf4j
@Service
public class VoucherBiz {

    @Autowired
    private ElecVoucherCategoryMapper voucherCategoryDao;

    @Autowired
    private ElecVoucherLibMapper ElecVoucherLibDao;

    /**
     * 根据券码类别查询是该类券码是否有效
     * @param categoryId
     * @return
     */
    public ElectronicVoucherCategory checkVoucherValidStatusByCategoryId(Long categoryId){
        log.info("根据券码类别id查询券码类别是否有效,categoryId:"+categoryId);
        ElecVoucherCategory elecVoucherCategory =voucherCategoryDao.getValidVoucherById(categoryId);
        ElectronicVoucherCategory electronicVoucherCategory = new ElectronicVoucherCategory();
        BeanCopierUtils.copyProperties(elecVoucherCategory,electronicVoucherCategory);
        return electronicVoucherCategory;
    }

    /**
     * 根据券码类别id，获取券码列表中券码
     * @param categoryId
     * @return
     */
    public ElectronicVoucherLib selectVoucherByCategoryId(Long categoryId){
        log.info("根据券码类别id获取一条有效券码,categoryId:"+categoryId);
        ElecVoucherLib elecVoucherLib = ElecVoucherLibDao.selectValidVoucherLibByCategoryId(categoryId);
        ElectronicVoucherLib ElectronicVoucherLib = new ElectronicVoucherLib();
        if(CommonUtils.isNullOrEmptyOfObj(elecVoucherLib)){
            log.info("券码列表查询不到该类别券码,categoryId:"+categoryId);
            throw new VoucherException(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getCode(),ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getMsg());
        }
        BeanCopierUtils.copyProperties(elecVoucherLib,ElectronicVoucherLib);
        return  ElectronicVoucherLib;
    }

    /**
     * 更新券码列表
     * @param electronicVoucherLib
     */
    public void UpdateElectronicVoucherLib(ElectronicVoucherLib electronicVoucherLib){
        log.info("更新券码信息列表,TicketNo:"+electronicVoucherLib.getTicketNo()+",StartDate:"+electronicVoucherLib.getStartDate());
        ElecVoucherLib elecVoucherLib = new ElecVoucherLib();
        BeanCopierUtils.copyProperties(electronicVoucherLib,elecVoucherLib);
        log.info(" Copy Message EndDate:"+electronicVoucherLib.getEndDate()+",TicketNo:"+electronicVoucherLib.getTicketNo()+",OrgNo:"+electronicVoucherLib.getOrgNo());
        ElecVoucherLibDao.UpdateElectronicVoucherLib(elecVoucherLib);
    }
}
