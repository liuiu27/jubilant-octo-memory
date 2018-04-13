package com.cupdata.config.biz;

import com.cupdata.sip.common.api.config.response.SysConfigVO;
import com.cupdata.sip.common.dao.entity.SysConfig;
import com.cupdata.sip.common.dao.mapper.SysConfigMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigBiz {

	@Autowired
	private SysConfigMapper sysConfigMapper;

	public SysConfigVO getSysConfig(String paraName, String bankCode) {

		SysConfigVO sysConfigVO =new SysConfigVO();
		SysConfig sysConfig =new SysConfig();
		sysConfig.setBankCode(bankCode);
		sysConfig.setParaNameEn(paraName);

		sysConfig = sysConfigMapper.selectOne(sysConfig);

		BeanCopierUtils.copyProperties(sysConfig,sysConfigVO);

		return sysConfigVO;

	}

	public List<SysConfigVO> selectAll() {
		List<SysConfig> sysConfigs = sysConfigMapper.selectAll();

		List<SysConfigVO> sysConfigVOS =new ArrayList<>(sysConfigs.size());

		sysConfigs.stream().forEach(sysConfig -> {

			SysConfigVO sysConfigVO = new SysConfigVO();
			BeanCopierUtils.copyProperties(sysConfig,sysConfigVO);
			sysConfigVOS.add(sysConfigVO);
		});

		return sysConfigVOS;
	}
}
