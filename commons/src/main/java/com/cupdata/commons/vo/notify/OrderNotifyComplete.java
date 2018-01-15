package com.cupdata.commons.vo.notify;

import java.util.Date;

import com.cupdata.commons.model.BaseModel;

import lombok.Data;

/**
 * @Auth: liwei
 * @Description: 回调成功表
 * @Date: 14:43 2018/1/11
 */
@Data
public class OrderNotifyComplete extends BaseModel{
	/**
	 * 订单编号
	 */
	 private String  orderNo;
	 
	 /**
	  * 异步通知路径
	  */
	 private String  notifyUrl;
	 
	 /**
	  * 完成时间
	  */
	 private Date  completeDate;
	 
	 /**
	  * 累积通知次数
	  */
	 private int  notifyTimes;
	 
	 /**
	  * 最终通知状态
	  */
	 private Character  notifyStatus;
	 
	 /**
	  * 节点名称
	  */
	 private String  nodeName;
}
