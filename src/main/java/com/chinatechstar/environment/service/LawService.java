package com.chinatechstar.environment.service;

import com.chinatechstar.environment.entity.*;
import com.chinatechstar.environment.uitl.Pager;
import com.chinatechstar.environment.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface LawService {
    Pager queryLawList(LawVo lawvo);

    List<Firm> queryFirmList();

    Pager<List<InspectVo>> queryInspect(InspectVo inspectVo);

    void addLaw(Law law);

    void updatelaw(Law law);

    void delLawById(Integer id);

    void updateInspectById(Inspect inspect);

    void delInspectById(Integer id);

    void addInspect(Inspect inspect);

    List<Firm> queryFirmByid(Integer id);

    List<Law> queryLawsire(LawVo lawvo);

    List<Law> queryLawParent(LawVo lawvo);

    List<Consult> queryConsult();

    void addConsult(Consult consult);

    Pager<List<Supplier>> queryPageSupplier(SupplierVo supplierVo);

    void addSupplier(SupplierVo supplierVo);


    void updateSupplier(SupplierVo supplierVo);

    void delSupplierById(Integer id);

    void addNeed(NeedVo needVo);

    String[] getFileContent(MultipartFile file) throws IOException;

    List<SupplierVo> getsupplieerName();

    List<SupplierVo> querySupplieerByName(SupplierVo supplierVo);

    Pager<List<Need>> queryPageNeedList(NeedVo needVo);

    void updateNeedById(NeedVo needVo);

    LawVo  queryLawById(Integer id);

    void delNeedById(Integer id);

    List<SupplierVo> querySupplierByType(SupplierVo supplierVo);

    void updateConsult(Consult consult);

    void delConsultById(Consult consult);

    Pager<List<ConsultVo>> queryConsultPage(ConsultVo consultVo);

    Pager<List<PictureVo>> queryPictutre(PictureVo pictureVo);

    void addPictutre(Picture picture);

    void updatePictutre(Picture picture);

    void delPictutreById(Integer id);

    List<Consult> queryConsultType();

    List<Consult> queryConsultById(Integer id);

    void addInspectUpdate(Inspect inspect);

    Pager<List<ImgsVo>> queryImgsPage(ImgsVo imgsVo);

    void updateImgs(ImgsVo imgsVo);

    void addImgs(ImgsVo imgsVo);

    void delImgs(Integer id);

    List<ImgsVo> getImgsByCode(Integer imgCode);
}
