package com.chinatechstar.admin.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.chinatechstar.component.commons.utils.PDFUtils;
import com.chinatechstar.component.commons.utils.WordUtils;
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

import com.chinatechstar.admin.entity.SysRole;
import com.chinatechstar.admin.service.SysRoleService;
import com.chinatechstar.admin.vo.SysRoleVO;
import com.chinatechstar.component.commons.result.ActionResult;
import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.utils.ExcelUtils;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import org.springframework.web.multipart.MultipartFile;

/**
 * 角色信息的控制层
 *
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
 */
@RestController
@RequestMapping("/sysrole")
public class SysRoleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询角色分页
     *
     * @param sysRoleVO 角色前端参数
     * @return
     */
    @GetMapping(path = "/querySysRole")
    public ListResult<Object> querySysRole(SysRoleVO sysRoleVO) {
        Map<String, Object> data = sysRoleService.querySysRole(sysRoleVO.getCurrentPage(), sysRoleVO.getPageSize(), sysRoleVO.getRoleName(),
                sysRoleVO.getRoleCode(), sysRoleVO.getRoleDescription(), sysRoleVO.getSorter());
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 查询角色名称的下拉框数据
     *
     * @param userId   用户ID
     * @param postCode 岗位编码
     * @param assign   是否授权（0是未授权，1是已授权）
     * @return
     */
    @GetMapping(path = "/queryRoleName")
    public ListResult<Object> queryRoleName(@RequestParam(name = "userId", required = false) Long userId,
                                            @RequestParam(name = "postCode", required = false) String postCode, @RequestParam(name = "assign", required = false) Short assign) {
        List<LinkedHashMap<String, Object>> data = sysRoleService.queryRoleName(userId, postCode, assign);
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 查询角色编码的下拉框数据
     *
     * @return
     */
    @GetMapping(path = "/queryRoleCode")
    public ListResult<Object> queryRoleCode() {
        List<LinkedHashMap<String, Object>> data = sysRoleService.queryRoleCode();
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 查询角色的多选框数据
     *
     * @return
     */
    @GetMapping(path = "/queryRoleNameCheckbox")
    public ListResult<Object> queryRoleNameCheckbox() {
        List<LinkedHashMap<String, Object>> data = sysRoleService.queryRoleNameCheckbox();
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 新增角色
     *
     * @param sysRole 角色对象
     * @return
     */
    @PostMapping(path = "/addSysRole")
    public ActionResult addSysRole(@Validated(InsertValidator.class) @RequestBody SysRole sysRole) {
        sysRoleService.insertSysRole(sysRole);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 将对应的用户授权给角色
     *
     * @param sysRoleVO 角色前端参数
     * @return
     */
    @PostMapping(path = "/authorizeUserToRole")
    public ActionResult authorizeUserToRole(@Validated(InsertValidator.class) @RequestBody SysRoleVO sysRoleVO) {
        sysRoleService.insertRoleIdUserId(sysRoleVO.getRoleId(), sysRoleVO.getUserId());
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 编辑角色
     *
     * @param sysRole 角色对象
     * @return
     */
    @PutMapping(path = "/updateSysRole")
    public ActionResult updateSysRole(@Validated(UpdateValidator.class) @RequestBody SysRole sysRole) {
        sysRoleService.updateSysRole(sysRole);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return
     */
    @PostMapping(path = "/deleteSysRole")
    public ActionResult deleteSysRole(@RequestParam(name = "id", required = true) Long[] id) {
        sysRoleService.deleteSysRole(id);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 根据查询条件导出角色到Excel
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @PostMapping(path = "/exportSysRole")
    public void exportSysRole(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            if (paramMap.get("isTemplate").equals("1")) { // 1为模板，0不为模板
                List<String> headList = Arrays.asList("角色编码", "角色名称", "角色描述");
                ExcelUtils.exportExcel(headList, null, "角色管理", response);
            } else {
                List<String> headList = Arrays.asList("ID", "角色编码", "角色名称", "角色描述", "创建时间");
                List<LinkedHashMap<String, Object>> dataList = sysRoleService.querySysRoleForExcel(paramMap);
                ExcelUtils.exportExcel(headList, dataList, "角色管理", response);
            }
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 导入角色
     *
     * @param file 文件资源
     * @return
     */
    @PostMapping(value = "/importSysRole", consumes = {"multipart/form-data"})
    public ActionResult importSysRole(@RequestParam(name = "file", required = true) MultipartFile file) {
        sysRoleService.importSysRole(file);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 根据查询条件导出角色到Word
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @PostMapping(path = "/exportWordSysRole")
    public void exportWordSysRole(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        exportCommonSysRole(response, paramMap, "Word");
    }

    /**
     * 根据查询条件导出角色到PDF
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @PostMapping(path = "/exportPDFSysRole")
    public void exportPDFSysRole(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        exportCommonSysRole(response, paramMap, "PDF");
    }

    /**
     * 根据查询条件导出角色到Word或PDF
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     * @param flag     Word或PDF
     */
    private void exportCommonSysRole(HttpServletResponse response, @RequestParam Map<String, Object> paramMap, String flag) {
        try {
            List<String> headList = Arrays.asList("角色编码", "角色名称", "角色描述", "创建时间");
            List<LinkedHashMap<String, Object>> dataList = sysRoleService.querySysRoleForExcel(paramMap);
            dataList.forEach(map -> {
                map.entrySet().removeIf(entry -> ("id".equals(entry.getKey())));
            });
            if (flag == "Word") {
                WordUtils.exportWord(headList, dataList, "角色管理", response);
            } else if (flag == "PDF") {
                PDFUtils.exportPDF(headList, dataList, "角色管理", response);
            }
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    /**
     * 查询当前用户的过滤数据字段
     *
     * @param menuCode 菜单编码
     * @param username 用户名
     * @return
     */
    @GetMapping(value = "/queryRoleData")
    public String queryRoleData(@RequestParam(name = "menuCode", required = true) String menuCode,
                                @RequestParam(name = "username", required = true) String username) {
        return sysRoleService.queryRoleData(menuCode, username);
    }

}
