package com.chinatechstar.environment.mapper;

import com.chinatechstar.environment.entity.*;
import com.chinatechstar.environment.uitl.Pager;
import com.chinatechstar.environment.vo.*;

import java.util.List;

public interface LawMapper {

    /**
     * 查询商品报表数据
     *
     * @return
     */
    List<LawVo> queryLawList(LawVo lawvo);

    List<Firm> queryFirmList();

    List<InspectVo> queryInspect(InspectVo inspectVo);

    void addLaw(Law law);

    void updatelaw(Law law);

    void delLawById(Integer id);

    void updateInspectById(Inspect inspect);

    void delInspectById(Integer id);

    void addInspect(Inspect inspect);

    List<Firm> queryFirmByid(Integer id);

    List<LawVo> queryLawListpage(LawVo lawvo);

    List<InspectVo> queryInspectpage(InspectVo inspectVo);

    List<Law> queryLawsire(LawVo lawvo);

    List<Law> queryLawParent(LawVo lawvo);

    List<Consult> queryConsult();

    void addConsult(Consult consult);

    List<Supplier> querySupplierList(SupplierVo supplierVo);

    List<Supplier> queryPageSupplier(SupplierVo supplierVo);

    void addSupplier(SupplierVo supplierVo);

    void updateSupplier(SupplierVo supplierVo);

    void delSupplierById(Integer id);

    void addNeed(NeedVo needVo);

    List<SupplierVo> getsupplieerName();

    List<SupplierVo> querySupplieerByName(SupplierVo supplierVo);

    List<Supplier> queryNeedList(NeedVo needVo);

    List<Need> queryPageNeedList(NeedVo needVo);

    void updateNeedById(NeedVo needVo);

    LawVo queryLawById(Integer id);

    void delNeedById(Integer id);

    List<SupplierVo> querySupplierByType(SupplierVo supplierVo);

    void updateConsult(Consult consult);

    void delConsultById(Consult consult);

    List<ConsultVo> queryConsultList(ConsultVo consultVo);

    List<ConsultVo> queryConsultPage(ConsultVo consultVo);

    List<PictureVo> queryPictutre(PictureVo pictureVo);

    List<PictureVo> queryPictutrepage(PictureVo pictureVo);

    void addPictutre(Picture picture);

    void updatePictutre(Picture picture);

    void delPictutreById(Integer id);

    List<Consult> queryConsultType();

    List<Consult> queryConsultById(Integer id);

    void addInspectUpdate(Inspect inspect);

    List<ImgsVo> queryImgs(ImgsVo imgsVo);

    List<ImgsVo> queryImgsPage(ImgsVo imgsVo);

    void updateImgs(ImgsVo imgsVo);

    void addImgs(ImgsVo imgsVo);

    void delImgs(Integer id);

    List<ImgsVo> getImgsByCode(Integer imgCode);
}
