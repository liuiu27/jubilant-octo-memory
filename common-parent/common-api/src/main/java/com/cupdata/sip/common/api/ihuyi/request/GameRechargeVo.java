package com.cupdata.sip.common.api.ihuyi.request;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Author: DingCong
 * @Description: 互亿游戏虚拟充值vo
 * @CreateDate: 2018/3/8 16:57
 */
public class GameRechargeVo {
	/**
	 * 游戏专区
	 */
	private String gameRegion;

	/**
	 * 游戏服务器
	 */
	private String gameServer;

	@JSONField(name = "GameRegion")
	public String getGameRegion() {
		return gameRegion;
	}

	public void setGameRegion(String gameRegion) {
		this.gameRegion = gameRegion;
	}

	@JSONField(name = "GameServer")
	public String getGameServer() {
		return gameServer;
	}

	public void setGameServer(String gameServer) {
		this.gameServer = gameServer;
	}

}
