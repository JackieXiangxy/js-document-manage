package com.js.download.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.js.download.manager.mapper.DownloadProcessMapper;
import com.js.download.manager.model.DownloadProcess;
import com.js.download.manager.service.DownloadProcessService;
import com.js.download.manager.util.QueryUtils;
import com.js.utils.aop.jwt.JwtTokenUtils;
import com.js.utils.jwt.dto.PowerModel;
import com.js.utils.page.PageUtils;
import com.js.utils.response.ResponseUtil;
import com.js.utils.response.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Transactional
@Service
public class DownloadProcessServiceImpl implements DownloadProcessService {

    @Resource
    private DownloadProcessMapper downloadMapper;


    public List<Object> getDataByUser(PageUtils<QueryUtils> page) {
        String userId = JwtTokenUtils.getUserToken().getUserPk();
        QueryUtils queryUtils = page.getQueryColumn();
            if (queryUtils.getFileType().equals("null")) queryUtils.setFileType(null);
        if (queryUtils.getStartTime().equals("")) queryUtils.setStartTime(null);
        if (queryUtils.getEndTime().equals("")) queryUtils.setEndTime(null);
        if (userId!=null) queryUtils.setUserId(userId);
        Integer total=downloadMapper.getTotalNum(queryUtils);
        Integer startPage=(page.getPageNum()-1)*page.getPageSize();
        Integer endPage=page.getPageNum()*page.getPageSize();
        if (total>=0){
            List<Map<String, Object>> map = downloadMapper.selectByUser(queryUtils,startPage,endPage);
            List<Object> jsons=new ArrayList<>();
            for (Map<String, Object> mapObject : map) {
                if (mapObject.get("FILE_TYPE").equals("DOC")){
                    mapObject.put("FILENAME",downloadMapper.getDocNoById(mapObject.get("DOWNLOAD_FILE_ID").toString()));
                }
                jsons.add(JSON.toJSON(mapObject));
            }

            return jsons;

        }
        return null;

    }

    @Override
    public Integer getTotalNum(PageUtils<QueryUtils> page) {
        String userId = JwtTokenUtils.getUserToken().getUserPk();
        QueryUtils queryUtils = page.getQueryColumn();
        if (queryUtils.getFileType().equals("null")) queryUtils.setFileType(null);
        if (queryUtils.getStartTime().equals("")) queryUtils.setStartTime(null);
        if (queryUtils.getEndTime().equals("")) queryUtils.setEndTime(null);
        if (userId!=null) queryUtils.setUserId(userId);
        return downloadMapper.getTotalNum(queryUtils);
    }

    @Override
    public int insertSelective(String insertData, String ip) {

        DownloadProcess download = new DownloadProcess();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        Map<String, Object> map = JSON.parseObject(insertData, Map.class);

        if (map.get("actionName") != null) {
            download.setActionName(map.get("actionName").toString());
        }
        if (map.get("processId") != null) {
            download.setProcessId(map.get("processId").toString());
        }
        if (map.get("fileType") != null) {
            download.setFileType(map.get("fileType").toString());
        }
        if (map.get("fileId") != null) {
            download.setDownloadFileId(map.get("fileId").toString());
        }
        download.setIpAdd(ip);
        download.setPkId(id);
        download.setIsDelete(0);
        download.setDownloadTime(new Date());
        download.setStatus(0);
        download.setUserId(JwtTokenUtils.getUserToken().getUserPk());
        download.setOrginizeId(JwtTokenUtils.getUserToken().getUserOrgId());
        int res = downloadMapper.insertSelective(download);
        return res;
    }

    @Override
    public String selectByUserIdFileId(String fileId) {
        String userId = JwtTokenUtils.getUserToken().getUserPk();//获取UserId
        List<DownloadProcess> downloadProcesses = downloadMapper.selectByUserIdFileId(userId, fileId);
        if (downloadProcesses.size() == 0) {
            return null;
        } else {
            String id = null;
            for (DownloadProcess download : downloadProcesses
            ) {
                id = download.getProcessId();
            }
            return id;
        }
    }

}
