package com.cupdata.notify.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cupdata.commons.utils.DateTimeUtil;

public class NotifyNextTime {
	/**
	 * 根据通知次数获取下次通知时间
	 * @param notifyTimes
	 * @return
	 */
	public static Date GetNextTime(Integer notifyTimes,Date time) {
		if(null == notifyTimes || null == time) {
			return null;
		}
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		map.put(1, 1);
		map.put(2, 1);
		map.put(3, 3);
		map.put(4, 10);
		map.put(5, 30);
		map.put(6, 30);
		map.put(7, 30);
		map.put(8, 30);
		map.put(9, 60);
		if(!map.containsKey(notifyTimes)) {
			return null;
		}
		Integer minutes = map.get(notifyTimes);
		return DateTimeUtil.addMinute(time, minutes);
	}
}
