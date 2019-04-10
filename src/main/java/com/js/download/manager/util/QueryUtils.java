package com.js.download.manager.util;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.js.utils.video.JsdzStringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryUtils implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="排序名称")
    private String sortName;
    @ApiModelProperty(value="排序类别（ ASC DESC）")
    private String sortOrder;

    @ApiModelProperty(value="文件类型")
    private String fileType;

    @ApiModelProperty(value="用户id")
    private String userId;

    /**
     * 以下查询条件，根据自己的业务和前端对接
     */
    @ApiModelProperty(value="更改开始时间")
    private String startTime;
    @ApiModelProperty(value="更改结束时间")
    private String endTime;

    /**
     * 拼接mybatisPlus查询条件
     * @return
     */
    public QueryWrapper<QueryUtils> queryWrap() {
        QueryWrapper<QueryUtils> column = new QueryWrapper<>();

        //只查询未注销数据
        column.eq("ZX_BZ", 0);
        //警情状态
        if (!StringUtils.isEmpty(fileType)) column.eq("fileType", fileType);
        if (!StringUtils.isEmpty(userId)) column.eq("userId", userId);

        //开始时间
        if(!StringUtils.isEmpty(startTime)) column.apply("download_time >=to_date('"+startTime+"','YYYY-MM-DD HH24:MI:SS')");
        //结束时间
        if(!StringUtils.isEmpty(endTime)) column.apply("download_time <=to_date('"+endTime+"','YYYY-MM-DD HH24:MI:SS')");

        if (!StringUtils.isEmpty(sortName)) {
            if ("ASC".equals(sortOrder.toUpperCase())) {
                column.orderByAsc(JsdzStringUtils.CoverLineStr(sortName));
            } else {
                column.orderByDesc(JsdzStringUtils.CoverLineStr(sortName));
            }
        }else {
            column.orderByDesc("download_time");
            column.orderByDesc("update_time");
        }
        return column;
    }
}
