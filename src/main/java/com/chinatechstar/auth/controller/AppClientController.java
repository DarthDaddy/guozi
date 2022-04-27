package com.chinatechstar.auth.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinatechstar.auth.entity.AppClient;
import com.chinatechstar.auth.service.AppClientService;
import com.chinatechstar.auth.vo.AppClientVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;

/**
 * 应用信息的控制层
 * 
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/appclient")
public class AppClientController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AppClientService appClientService;

	/**
	 * 查询应用分页
	 * 
	 * @param appClientVO 应用前端参数
	 * @return
	 */
	@GetMapping(path = "/queryAppClient")
	public ListResult<Object> queryAppClient(AppClientVO appClientVO) {
		Map<String, Object> data = appClientService.queryAppClient(appClientVO.getCurrentPage(), appClientVO.getPageSize(), appClientVO.getClientCode(),
				appClientVO.getAuthType(), appClientVO.getClientSecret(), appClientVO.getAuthScope(), appClientVO.getTokenSeconds(),
				appClientVO.getRefreshSeconds(), appClientVO.getFallbackUrl(), appClientVO.getClientDescription(), appClientVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增应用
	 * 
	 * @param appClient 应用对象
	 * @return
	 */
	@PostMapping(path = "/addAppClient")
	public ActionResult addAppClient(@Validated(InsertValidator.class) @RequestBody AppClient appClient) {
		appClientService.insertAppClient(appClient);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑应用
	 * 
	 * @param appClient 应用对象
	 * @return
	 */
	@PutMapping(path = "/updateAppClient")
	public ActionResult updateAppClient(@Validated(UpdateValidator.class) @RequestBody AppClient appClient) {
		appClientService.updateAppClient(appClient);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除应用
	 * 
	 * @param id 应用ID
	 * @return
	 */
	@PostMapping(path = "/deleteAppClient")
	public ActionResult deleteAppClient(@RequestParam(name = "id", required = true) Long[] id) {
		appClientService.deleteAppClient(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出应用
	 * 
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportAppClient")
	public void exportAppClient(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "应用编码", "应用密钥", "授权类型", "授权范围", "令牌秒数", "刷新秒数", "回调地址", "应用描述", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = appClientService.queryAppClientForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "应用管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
