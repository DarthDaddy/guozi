package com.chinatechstar.auth.vo;

import java.io.Serializable;

import com.chinatechstar.component.commons.vo.CommonVO;

/**
 * 应用信息的参数类
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
public class AppClientVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = -7365233858896287067L;
	private String clientCode;// 应用编码
	private String authType;// 授权类型
	private String clientSecret;// 应用密钥
	private String authScope;// 授权范围
	private Long tokenSeconds;// 令牌秒数
	private Long refreshSeconds;// 刷新秒数
	private String fallbackUrl;// 回调地址
	private String clientDescription;// 应用描述

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getAuthScope() {
		return authScope;
	}

	public void setAuthScope(String authScope) {
		this.authScope = authScope;
	}

	public Long getTokenSeconds() {
		return tokenSeconds;
	}

	public void setTokenSeconds(Long tokenSeconds) {
		this.tokenSeconds = tokenSeconds;
	}

	public Long getRefreshSeconds() {
		return refreshSeconds;
	}

	public void setRefreshSeconds(Long refreshSeconds) {
		this.refreshSeconds = refreshSeconds;
	}

	public String getFallbackUrl() {
		return fallbackUrl;
	}

	public void setFallbackUrl(String fallbackUrl) {
		this.fallbackUrl = fallbackUrl;
	}

	public String getClientDescription() {
		return clientDescription;
	}

	public void setClientDescription(String clientDescription) {
		this.clientDescription = clientDescription;
	}

}
