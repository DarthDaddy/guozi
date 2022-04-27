package com.chinatechstar.gateway.config;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;

/**
 * Sentinel高可用流控防护组件配置
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@Configuration
public class GatewayRuleConfig {

	@PostConstruct
	public void doInit() {
		// Prepare some gateway rules and API definitions (only for demo).
		// It's recommended to leverage dynamic data source or the Sentinel dashboard to push the rules.
		initCustomizedApis();
		initGatewayRules();
	}

	private void initCustomizedApis() {
		Set<ApiDefinition> definitions = new HashSet<>();
		ApiDefinition api1 = new ApiDefinition("filter_api").setPredicateItems(new HashSet<ApiPredicateItem>() {
			{
				add(new ApiPathPredicateItem().setPattern("/api/uaa/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/account/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/admin/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/chart/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/file/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/notification/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/statistics/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/activiti/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
				add(new ApiPathPredicateItem().setPattern("/api/generator/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
			}
		});
		ApiDefinition api2 = new ApiDefinition("another_api").setPredicateItems(new HashSet<ApiPredicateItem>() {
			{
				add(new ApiPathPredicateItem().setPattern("/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
			}
		});
		definitions.add(api1);
		definitions.add(api2);
		GatewayApiDefinitionManager.loadApiDefinitions(definitions);
	}

	private void initGatewayRules() {
		Set<GatewayFlowRule> rules = new HashSet<>();

		rules.add(new GatewayFlowRule("mscode-auth").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-auth").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-account-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-account-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-admin-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-admin-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-chart-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-chart-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-file-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-file-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-notification-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-notification-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-statistics-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-statistics-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-activiti-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-activiti-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		rules.add(new GatewayFlowRule("mscode-generator-service").setCount(1000).setIntervalSec(1));
		rules.add(new GatewayFlowRule("mscode-generator-service").setCount(200).setIntervalSec(2).setBurst(200)
				.setParamItem(new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));

		GatewayRuleManager.loadRules(rules);
	}

}