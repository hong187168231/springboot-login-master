package com.alibaba.bean;

import io.swagger.annotations.ApiModelProperty;

public class PageRequest {
    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private int pageNum = 1;
    /**
     * 每页数量
     */
    @ApiModelProperty("每页数量")
    private int pageSize = 10;

    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
