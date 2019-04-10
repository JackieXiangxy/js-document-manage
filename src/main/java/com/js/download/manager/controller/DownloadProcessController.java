package com.js.download.manager.controller;


import com.js.download.manager.service.DownloadProcessService;
import com.js.download.manager.util.QueryUtils;
import com.js.utils.aop.jwt.JwtTokenUtils;
import com.js.utils.jwt.dto.PowerModel;
import com.js.utils.page.PageUtils;
import com.js.utils.response.ResponseUtil;
import com.js.utils.response.Result;
import com.js.utils.tool.IpTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("download")
@Api(value="警情管理模块",description="相关接口信息")
public class DownloadProcessController {

    protected final static Logger logger = LoggerFactory.getLogger(DownloadProcessController.class);


    @Resource
    private DownloadProcessService processService;


    @PostMapping("getDataByUser")
    @ApiOperation(value = "获取数据", notes = "参数格式：{ insertData：{ id:12345, name:'张三' } }")
    public Result<Object> getDataByUser(HttpServletRequest request){
        //权限控制
        PowerModel userToken = JwtTokenUtils.getUserToken();
        if (userToken == null) return ResponseUtil.fall("登录失效,请重新登录!");
        PageUtils page=new PageUtils<>(QueryUtils.class, request);
        Long total= Long.valueOf(processService.getTotalNum(new PageUtils<>(QueryUtils.class, request)));
        List<Object> resultData=processService.getDataByUser(new PageUtils<>(QueryUtils.class, request));
        return ResponseUtil.ok(page.getResultMap(resultData,total));
    }


    @PostMapping("insertDatas")
    @ApiOperation(value = "保存下载请求", notes = "参数格式：{ insertData：{ id:12345, name:'张三' } }")
    public Result<Object> insertDownloadDatas(HttpServletRequest request, String insertData){
        String ip= IpTool.getIpAddr(request);
        return ResponseUtil.ok(processService.insertSelective(insertData,ip));
    }


    @PostMapping("getdownObject")
    @ApiOperation(value = "通过用户和文件获取已经存在的流程", notes = "参数格式：{ userId：用户id;fileId:文件id }")
    public String getdownObject( String fileId){
        String returnData=processService.selectByUserIdFileId(fileId);
        return returnData;
    }




}
