package com.chinatechstar.environment.controller;

import com.chinatechstar.component.commons.result.ListResult;
import com.chinatechstar.component.commons.result.ResultBuilder;
import com.chinatechstar.component.commons.validator.InsertValidator;
import com.chinatechstar.component.commons.validator.UpdateValidator;
import com.chinatechstar.environment.entity.*;
import com.chinatechstar.environment.service.LawService;
import com.chinatechstar.environment.uitl.Pager;
import com.chinatechstar.environment.vo.*;
import com.chinatechstar.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("law")
public class LawController {
    @Autowired
    private LawService lawService;
    @Autowired
    private FileService fileService;

    /**
     * 根据类型查询供应商无分页
     * @param supplierVo
     * @return
     */
    @GetMapping("querySupplierByType")
    public ListResult<Object> querySupplierByType(SupplierVo supplierVo){
        List<SupplierVo> supplierVoList = lawService.querySupplierByType(supplierVo);
        return ResultBuilder.buildListSuccess ( supplierVoList );
    }

    /**
     * 需求删除
     * @param id
     * @return
     */

    @GetMapping("delNeedById")
    public ListResult<Object> delNeedById(Integer id){
        lawService.delNeedById(id);
        return ResultBuilder.buildListSuccess ( "删除成功" );
    }

    /**
     * 通过id查询法制法规详情信息
     * @param id
     * @return
     */
    @GetMapping("queryLawById")
    public ListResult<Object> queryLawById(Integer id){
        LawVo law = lawService.queryLawById(id);
        List<LawVo> lawVos= new ArrayList<> ();
        lawVos.add ( law );
        return ResultBuilder.buildListSuccess (  lawVos );
    }

    /**
     * 修改需求
     * @param needVo
     * @return
     */
    @PostMapping("updateNeedById")
    public ListResult<Object> updateNeedById(@Validated(UpdateValidator.class)@RequestBody NeedVo needVo){
        lawService.updateNeedById(needVo);
        return ResultBuilder.buildListSuccess ("修改成功");
    }



    /**
     * 需求分页查询
     * @param needVo
     * @return
     */
    @GetMapping("queryPageNeedList")
    public Pager<List<Need>> queryPageNeedList(NeedVo needVo){
        Pager<List<Need>> listPage = lawService.queryPageNeedList(needVo);
        return listPage;
    }
    /**
     * 新增需求
     * @param needVo
     * @return
     */
    @PostMapping("addNeed")
    public ListResult<Object> addNeed(@Validated(InsertValidator.class)@RequestBody NeedVo needVo){
        lawService.addNeed(needVo);
        return ResultBuilder.buildListSuccess ( "新增成功" );
    }

    /**
     * pdf文件读取返回
     * @param file
     * @return
     */
    @PostMapping("getFileContent")
    public ListResult<Object> getFileContent(MultipartFile file) throws IOException {
        String[] lines =lawService.getFileContent(file);
        return ResultBuilder.buildListSuccess ( Arrays.asList ( lines ) );
    }
    /**
     * 删除供销商信息
     * @param id
     * @return
     */
    @GetMapping("delSupplierById")
    public ListResult<Object> delSupplierById(Integer id){
        lawService.delSupplierById(id);
        return ResultBuilder.buildListSuccess ( "删除成功" );
    }

    /**
     * 给供销商类型分类
     * @return
     */
    @GetMapping("getsupplieerName")
    public ListResult<Object> getsupplieerName(){
        List<SupplierVo> supplierVoList = lawService.getsupplieerName();
        return ResultBuilder.buildListSuccess ( supplierVoList);
    }

    /**
     * 供销商条件查询
     * @param supplierVo
     * @return
     */
    @GetMapping("querySupplieer")
    public ListResult<Object> querySupplieer(SupplierVo supplierVo){
        List<SupplierVo> supplierVoList = lawService.querySupplieerByName(supplierVo);
        return ResultBuilder.buildListSuccess ( supplierVoList );
    }
    /**
     * 修改供销商信息
     * @param supplierVo
     * @return
     */
    @PostMapping("updateSupplier")
    public ListResult<Object> updateSupplier(@Validated(UpdateValidator.class)@RequestBody SupplierVo supplierVo){
        lawService.updateSupplier(supplierVo);
        return ResultBuilder.buildListSuccess ( "修改成功" );
    }

    /**
     * 新增供销商信息
     * @paramsupplierVo
     * @return
     */
    @PostMapping("addSupplier")
    public ListResult<Object> addSupplier(@Validated(InsertValidator.class)@RequestBody SupplierVo supplierVo){
        lawService.addSupplier(supplierVo);
        return ResultBuilder.buildListSuccess ( "新增成功" );
    }


    /**
     * 查询供销商分页信息
     * @param supplierVo
     * @return
     */
    @GetMapping("queryPageSupplier")
    public Pager<List<Supplier>> queryPageSupplier(SupplierVo supplierVo){
        Pager<List<Supplier>> supplier= lawService.queryPageSupplier(supplierVo);
        return supplier;
    }

    /**
     * 查询法规详情
     * @return
     */
    @GetMapping("queryLaw")
    public Pager<List<Law>> queryLaw(LawVo lawvo){
        Pager<List<Law>> pager = lawService.queryLawList(lawvo);
        return pager;
    }
    /**
     * 查询法规父级详情
     * @return
     */
    @GetMapping("queryLawParent")
    public ListResult<Object> queryLawParent(LawVo lawvo){
        List<Law> laws = lawService.queryLawParent(lawvo);
        return ResultBuilder.buildListSuccess ( laws );
    }

    /**
     * 查询法规详情
     * @return
     */
    @GetMapping("queryLawsire")
    public ListResult<Object> queryLawsire(LawVo lawvo){
        List<Law> lawList = lawService.queryLawsire(lawvo);
        return ResultBuilder.buildListSuccess (lawList );
    }
        /**
         * 新增法制法规
     *
     */
    @PostMapping("addLaw")
    public ListResult<Object> addLaw(@Validated(InsertValidator.class)@RequestBody Law law){
        lawService.addLaw(law);
        return ResultBuilder.buildListSuccess ( "新增成功" );
    }
    /**
     * 修改法制法规
     *
     */
    @PostMapping("updatelaw")
    public ListResult<Object> updatelaw(@Validated(UpdateValidator.class)@RequestBody Law law){
        lawService.updatelaw(law);
        return ResultBuilder.buildListSuccess ( "修改成功" );
    }
    /**
     * 根据id删除法制法规
     */
    @PostMapping("delLawById")
    public ListResult<Object> delLawById(@RequestParam(name = "id", required = true) Integer id){
        lawService.delLawById(id);
        return ResultBuilder.buildListSuccess ( "删除成功" );
    }

    /**
     * 查询企业列表
     *
     */
    @GetMapping("queryFirmList")
    public ListResult<Object> queryFirmList(){
        List<Firm> firmList=lawService.queryFirmList();
        return ResultBuilder.buildListSuccess ( firmList );
    }
    /**
     * 通过id查询企业
     *
     */
    @GetMapping("queryFirmByid")
    public ListResult<Object> queryFirmByid(Integer id){
        List<Firm> firmList=lawService.queryFirmByid(id);
        return ResultBuilder.buildListSuccess ( firmList );
    }
    /**
         * 查询环保体检报告
     */
    @GetMapping("queryInspectByFirmId")
    public Pager<List<InspectVo>> queryInspectByFirmId( InspectVo inspectVo){
        Pager<List<InspectVo>> inspectList = lawService.queryInspect(inspectVo);
        return inspectList;
    }

    /**
     * 修改环境体检报告
     * @param inspect 环境实体类接收数据
     * @return
     */
    @PostMapping("updateInspectById")
    public  ListResult<Object> updateInspectById(@Validated(UpdateValidator.class)@RequestBody Inspect inspect){
        lawService.updateInspectById(inspect);
        return ResultBuilder.buildListSuccess ( "修改成功" );
    }

    /**
     * 删除企业体检报告
     * @param id 体检报告唯一主键
     * @return
     */

    @PostMapping("delInspectById")
    public ListResult<Object> delInspectById(@RequestParam(name = "id", required = true) Integer id){
        lawService.delInspectById(id);
        return ResultBuilder.buildListSuccess ( "删除成功" );
    }

    /**
     * 新增企业体检报告
     * @param inspect
     * @return
     */
    @PostMapping("addInspect")
    public ListResult<Object> addInspect(@Validated(InsertValidator.class)@RequestBody Inspect inspect){
        lawService.addInspect(inspect);
        return ResultBuilder.buildListSuccess ( "新增成功" );
    }
    /**
     *上传体检处理报告
     */
    @PostMapping("addInspectUpdate")
    public ListResult<Object> addInspectUpdate(@Validated(InsertValidator.class)@RequestBody Inspect inspect){
        lawService.addInspectUpdate(inspect);
        return ResultBuilder.buildListSuccess ( "体检处理报告上传成功" );
    }

    /**
     * 查询咨询
     * @return
     */
    @GetMapping("queryConsult")
    public ListResult<Object> queryConsult(){
        List<Consult> consultList = lawService.queryConsult();
        return ResultBuilder.buildListSuccess ( consultList);
    }
    /**
     * 通过id查询咨询
     * @return
     */
    @GetMapping("queryConsultById")
    public ListResult<Object> queryConsultById(Integer id){
        List<Consult> consultList = lawService.queryConsultById(id);
        return ResultBuilder.buildListSuccess ( consultList);
    }
    /**
     * 查询分页咨询
     * @return
     */
    @GetMapping("queryConsultPage")
    public Pager<List<ConsultVo>> queryConsultPage(ConsultVo consultVo){
        Pager<List<ConsultVo>> consultList = lawService.queryConsultPage(consultVo);
        return  consultList;
    }

    /**
     * 新增咨询
     * @param consult
     * @return
     */
    @PostMapping("addConsult")
    public ListResult<Object> addConsult(@Validated(InsertValidator.class)@RequestBody Consult consult){
        lawService.addConsult(consult);
        return ResultBuilder.buildListSuccess ( "新增成功" );
    }

    /**
     * 修改咨询
     * @param consult
     * @return
     */
    @PostMapping("updateConsult")
    public ListResult<Object> updateConsult(@Validated(UpdateValidator.class)@RequestBody Consult consult){
        lawService.updateConsult(consult);
        return ResultBuilder.buildListSuccess ( "修改成功" );
    }

    /**
     * 咨询删除
     * @param consult
     * @return
     */
    @GetMapping("delConsultById")
    public ListResult<Object> delConsultById(Consult consult){
        lawService.delConsultById(consult);
        return ResultBuilder.buildListSuccess ( "删除成功" );
    }

    /**
     *分页查询图片
     * @param pictureVo
     * @return
     */
    @GetMapping("queryPictutre")
    public Pager<List<PictureVo>> queryPictutre(PictureVo pictureVo){
        Pager<List<PictureVo>> pictureList = lawService.queryPictutre(pictureVo);
        return pictureList;
    }
    @PostMapping("addPictutre")
    public ListResult<Object> addPictutre(@Validated(InsertValidator.class)@RequestBody Picture picture){
        lawService.addPictutre(picture);
        return ResultBuilder.buildListSuccess ( "新增成功" );
    }
    @PostMapping("updatePictutre")
    public ListResult<Object> updatePictutre(@Validated(UpdateValidator.class)@RequestBody Picture picture){
        lawService.updatePictutre(picture);
        return ResultBuilder.buildListSuccess ( "修改成功" );
    }
    @GetMapping("delPictutreById")
    public ListResult<Object> delPictutreById(Integer id){
        lawService.delPictutreById(id);
        return ResultBuilder.buildListSuccess ( "删除成功" );
    }
    @GetMapping("queryConsultType")
    public ListResult<Object> queryConsultType(){
        List<Consult> list = lawService.queryConsultType();
        return ResultBuilder.buildListSuccess ( list );
    }

    /**
     * 首页照片分页查询
     * @param imgsVo
     * @return
     */
    @GetMapping("queryImgsPage")
    public Pager<List<ImgsVo>> queryImgsPage(ImgsVo imgsVo){
        Pager<List<ImgsVo>> imgPageList = lawService.queryImgsPage(imgsVo);
        return imgPageList;
    }

    /**
     * 修改首页照片
     * @param imgsVo
     * @return
     */
    @PostMapping("updateImgs")
    public ListResult<Object> updateImgs(@Validated(UpdateValidator.class)@RequestBody ImgsVo imgsVo){
        lawService.updateImgs(imgsVo);
        return ResultBuilder.buildListSuccess ( "修改成功" );
    }

    /**
     * 新增首页照片
     * @param imgsVo
     * @return
     */
    @PostMapping("addImgs")
    public ListResult<Object> addImgs(@Validated(InsertValidator.class)@RequestBody ImgsVo imgsVo){
        lawService.addImgs(imgsVo);
        return ResultBuilder.buildListSuccess ( "新增成功" );
    }

    /**
     * 删除首页照片
     * @param id
     * @return
     */
    @GetMapping("delImgs")
    public ListResult<Object> delImgs(Integer id){
        lawService.delImgs(id);
        return ResultBuilder.buildListSuccess ( "删除成功" );
    }

    @GetMapping("getImgsByCode")
    public ListResult<Object> getImgsByCode(){
        List<ImgsVo> imgsVoList= lawService.getImgsByCode(1);
        return ResultBuilder.buildListSuccess ( imgsVoList );
    }
}
