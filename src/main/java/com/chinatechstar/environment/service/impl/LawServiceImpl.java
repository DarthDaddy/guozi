package com.chinatechstar.environment.service.impl;

import com.chinatechstar.component.commons.utils.CurrentUserUtils;
import com.chinatechstar.environment.entity.*;
import com.chinatechstar.environment.mapper.LawMapper;
import com.chinatechstar.environment.service.LawService;
import com.chinatechstar.environment.uitl.Pager;
import com.chinatechstar.environment.vo.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class LawServiceImpl implements LawService {

    @Autowired
    private LawMapper lawMapper;

    @Override
    public Pager queryLawList(LawVo lawvo) {
        List<LawVo> lawVos = lawMapper.queryLawList (lawvo);
        Pager<Object> pager= new  Pager<Object>();
        pager.setCurrentPage ( lawvo.getCurrentPage () );
        pager.setPageSize ( lawvo.getPageSize () );
        pager.setRecordTotal ( lawVos.size () );
        int currentPage= lawvo.getPageSize () * (lawvo.getCurrentPage () - 1);
        lawvo.setPageSize ( lawvo.getPageSize ()*lawvo.getCurrentPage () );
        lawvo.setCurrentPage ( currentPage );
        List<LawVo> lawVoss = lawMapper.queryLawListpage (lawvo);
        pager.setContent ( Collections.singletonList ( lawVoss ) );
        return pager;
    }


    @Override
    public List<Firm> queryFirmList() {
        return lawMapper.queryFirmList();
    }

    @Override
    public  Pager<List<InspectVo>> queryInspect(InspectVo inspectVo) {
        List<InspectVo> inspectList = lawMapper.queryInspect ( inspectVo );
        Pager<List<InspectVo>> pager= new  Pager<List<InspectVo>>();
        pager.setCurrentPage ( inspectVo.getCurrentPage () );
        pager.setPageSize ( inspectVo.getPageSize () );
        pager.setRecordTotal ( inspectList.size () );
        int pageSize = inspectVo.getPageSize () * inspectVo.getCurrentPage ();
        inspectVo.setCurrentPage ( inspectVo.getPageSize ()*(inspectVo.getCurrentPage ()-1 ));
        inspectVo.setPageSize (pageSize );
        List<InspectVo> inspectLists = lawMapper.queryInspectpage ( inspectVo );
        pager.setContent ( Collections.singletonList ( inspectLists ) );
        return pager;
    }

    @Override
    public void addLaw(Law law) {
        lawMapper.addLaw(law);
    }

    @Override
    public void updatelaw(Law law) {
        lawMapper.updatelaw(law);
    }

    @Override
    public void delLawById(Integer id) {
        lawMapper.delLawById(id);
    }

    @Override
    public void updateInspectById(Inspect inspect) {
        lawMapper.updateInspectById(inspect);
    }

    @Override
    public void delInspectById(Integer id) {
        lawMapper.delInspectById(id);
    }

    @Override
    public void addInspect(Inspect inspect) {
        inspect.setInspectCode ( 1 );
        lawMapper.addInspect(inspect);
    }

    @Override
    public List<Firm> queryFirmByid(Integer id) {
        return lawMapper.queryFirmByid(id);
    }

    @Override
    public List<Law> queryLawsire(LawVo lawvo) {
        List<Law> laws = lawMapper.queryLawsire ( lawvo );
        List<Law> lawsss=new ArrayList<Law> ();
        for(int i=0;i<laws.size ();i++){
            if(laws.get ( i ).getpId ()==0){
                List<Law> lawss=new ArrayList<Law> ();
                for (int j=1 ;j<laws.size ();j++){
                    if(laws.get ( i ).getId ()==laws.get ( j ).getpId ()){
                        lawss.add ( laws.get ( j ) );
                    }
                }
                laws.get ( i ).setLawList ( lawss );
                lawsss.add ( laws.get ( i ) );
            }else {
                lawsss.add ( laws.get ( i ) );
            }
        }
        return lawsss;
    }

    @Override
    public List<Law> queryLawParent(LawVo lawvo) {
        return lawMapper.queryLawParent(lawvo);
    }

    @Override
    public List<Consult> queryConsult() {
        return lawMapper.queryConsult();
    }

    @Override
    public void addConsult(Consult consult) {
        lawMapper.addConsult(consult);
    }

    @Override
    public Pager<List<Supplier>> queryPageSupplier(SupplierVo supplierVo) {
        List<Supplier> suppliers = lawMapper.querySupplierList(supplierVo);
        Pager<List<Supplier>> pager= new  Pager<List<Supplier>>();
        pager.setCurrentPage ( supplierVo.getCurrentPage () );
        pager.setPageSize ( supplierVo.getPageSize () );
        pager.setRecordTotal ( suppliers.size () );
        int pageSize = supplierVo.getPageSize () * supplierVo.getCurrentPage ();
        supplierVo.setCurrentPage ( supplierVo.getPageSize ()*(supplierVo.getCurrentPage ()-1 ));
        supplierVo.setPageSize (pageSize );
        List<Supplier> supplierList = lawMapper.queryPageSupplier ( supplierVo );
        pager.setContent ( Collections.singletonList ( supplierList ) );
        return pager;
    }

    @Override
    public void addSupplier(SupplierVo supplierVo) {
        lawMapper.addSupplier(supplierVo);
    }

    @Override
    public void updateSupplier(SupplierVo supplierVo) {
        lawMapper.updateSupplier(supplierVo);
    }

    @Override
    public void delSupplierById(Integer id) {
        lawMapper.delSupplierById(id);
    }

    @Override
    public void addNeed(NeedVo needVo) {
        lawMapper.addNeed(needVo);
    }

    @Override
    public String[] getFileContent(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        try (PDDocument document = PDDocument.load(inputStream)){

            document.getClass();

            if(!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);

                String[] lines = pdfFileInText.split("\\r?\\n");
                for(String line : lines) {
                    System.out.println(line);
                }
                return lines;
            }

        } catch (InvalidPasswordException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SupplierVo> getsupplieerName() {
        return lawMapper.getsupplieerName();
    }

    @Override
    public List<SupplierVo> querySupplieerByName(SupplierVo supplierVo) {



        return lawMapper.querySupplieerByName(supplierVo);
    }

    /**
     * 需求分页查询
     * @param needVo
     * @return
     */
    @Override
    public Pager<List<Need>> queryPageNeedList(NeedVo needVo) {
        List<Supplier> needList = lawMapper.queryNeedList(needVo);
        Pager<List<Need>> pager= new  Pager<List<Need>>();
        pager.setCurrentPage ( needVo.getCurrentPage () );
        pager.setPageSize ( needVo.getPageSize () );
        pager.setRecordTotal ( needList.size () );
        int pageSize = needVo.getPageSize () * needVo.getCurrentPage ();
        needVo.setCurrentPage ( needVo.getPageSize ()*(needVo.getCurrentPage ()-1 ));
        needVo.setPageSize (pageSize );
        List<Need> needLists = lawMapper.queryPageNeedList ( needVo );
        pager.setContent ( Collections.singletonList ( needLists ) );
        return pager;
    }

    @Override
    public void updateNeedById(NeedVo needVo) {
        lawMapper.updateNeedById(needVo);
    }

    @Override
    public LawVo queryLawById(Integer id) {
        return lawMapper.queryLawById(id);
    }

    @Override
    public void delNeedById(Integer id) {
        lawMapper.delNeedById(id);
    }

    @Override
    public List<SupplierVo> querySupplierByType(SupplierVo supplierVo) {
        return lawMapper.querySupplierByType(supplierVo);
    }

    @Override
    public void updateConsult(Consult consult) {
        lawMapper.updateConsult(consult);
    }

    @Override
    public void delConsultById(Consult consult) {
        lawMapper.delConsultById(consult);
    }

    @Override
    public Pager<List<ConsultVo>> queryConsultPage(ConsultVo consultVo) {
        List<ConsultVo> suppliers = lawMapper.queryConsultList (consultVo);
        Pager<List<ConsultVo>> pager= new  Pager<List<ConsultVo>>();
        pager.setCurrentPage ( consultVo.getCurrentPage () );
        pager.setPageSize ( consultVo.getPageSize () );
        pager.setRecordTotal ( suppliers.size () );
        int pageSize = consultVo.getPageSize () * consultVo.getCurrentPage ();
        consultVo.setCurrentPage ( consultVo.getPageSize ()*(consultVo.getCurrentPage ()-1 ));
        consultVo.setPageSize (pageSize );
        List<ConsultVo> consultVoList = lawMapper.queryConsultPage ( consultVo );
        pager.setContent ( Collections.singletonList ( consultVoList ) );
        return pager;
    }

    @Override
    public Pager<List<PictureVo>> queryPictutre(PictureVo pictureVo) {
        List<PictureVo> pictureVoList = lawMapper.queryPictutre (pictureVo);
        Pager<List<PictureVo>> pager= new  Pager<List<PictureVo>>();
        pager.setCurrentPage ( pictureVo.getCurrentPage () );
        pager.setPageSize ( pictureVo.getPageSize () );
        pager.setRecordTotal ( pictureVoList.size () );
        int pageSize = pictureVo.getPageSize () * pictureVo.getCurrentPage ();
        pictureVo.setCurrentPage ( pictureVo.getPageSize ()*(pictureVo.getCurrentPage ()-1 ));
        pictureVo.setPageSize (pageSize );
        List<PictureVo> pictureList = lawMapper.queryPictutrepage ( pictureVo );
        pager.setContent ( Collections.singletonList ( pictureList ) );
        return pager;
    }

    @Override
    public void addPictutre(Picture picture) {
        lawMapper.addPictutre(picture);
    }

    @Override
    public void updatePictutre(Picture picture) {
        lawMapper.updatePictutre(picture);
    }

    @Override
    public void delPictutreById(Integer id) {
        lawMapper.delPictutreById(id);
    }

    @Override
    public List<Consult> queryConsultType() {
        return lawMapper.queryConsultType();
    }

    @Override
    public List<Consult> queryConsultById(Integer id) {
        return lawMapper.queryConsultById(id);
    }

    @Override
    public void addInspectUpdate(Inspect inspect) {
        inspect.setInspectCode ( 2 );
        lawMapper.addInspectUpdate(inspect);
    }

    @Override
    public Pager<List<ImgsVo>> queryImgsPage(ImgsVo imgsVo) {
        List<ImgsVo> imgsVoList = lawMapper.queryImgs(imgsVo);
        Pager<List<ImgsVo>> pager= new  Pager<List<ImgsVo>>();
        pager.setCurrentPage ( imgsVo.getCurrentPage () );
        pager.setPageSize ( imgsVo.getPageSize () );
        pager.setRecordTotal ( imgsVoList.size () );
        int pageSize = imgsVo.getPageSize () * imgsVo.getCurrentPage ();
        imgsVo.setCurrentPage ( imgsVo.getPageSize ()*(imgsVo.getCurrentPage ()-1 ));
        imgsVo.setPageSize (pageSize );
        List<ImgsVo> imgsVoList1 = lawMapper.queryImgsPage ( imgsVo );
        pager.setContent ( Collections.singletonList ( imgsVoList1 ) );
        return pager;
    }

    @Override
    public void updateImgs(ImgsVo imgsVo) {
        lawMapper.updateImgs(imgsVo);
    }

    @Override
    public void addImgs(ImgsVo imgsVo) {
        lawMapper.addImgs(imgsVo);
    }

    @Override
    public void delImgs(Integer id) {
        lawMapper.delImgs(id);
    }

    @Override
    public List<ImgsVo> getImgsByCode(Integer imgCode) {
        return lawMapper.getImgsByCode(imgCode);
    }


}
