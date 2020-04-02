package com.company.project.framework.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * bootstrap table用到的返回json格式
 * 分页
 **/
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PageResult {
    /**
     * 总记录数
     */
    private Long total = 0L;
    /**
     * 当前第几页
     */
    private Long current = 0L;
    /**
     * 总页数
     */
    private Long pages = 0L;
    /**
     * 每页记录数
     */
    private Long size = 0L;
    /**
     * 当前页记录数
     */
    private Integer curPageSize = 0;
    /**
     * 数据列表
     */
    private List rows;


    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult() {
    }
}

