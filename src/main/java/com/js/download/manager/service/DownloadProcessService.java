package com.js.download.manager.service;


import com.js.download.manager.util.QueryUtils;
import com.js.utils.page.PageUtils;
import com.js.utils.response.Result;

import java.util.List;
import java.util.Map;

public interface DownloadProcessService {

    public int insertSelective(String insertData, String ip);

    public String selectByUserIdFileId( String fileId);

    public List<Object> getDataByUser(PageUtils<QueryUtils> page);

    public  Integer getTotalNum(PageUtils<QueryUtils> page);


}
