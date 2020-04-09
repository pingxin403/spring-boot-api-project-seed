package com.company.project.framework.object;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 分页的VO
 * @author hyp
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseVO {
    public final static int DEFAULT_PAGE_SIZE = 10;
    @ApiModelProperty(value = "第几页")
    private int pageNumber = 1;
    @ApiModelProperty(value = "分页数量")
    private int pageSize = 0;
    private int pageStart = 0;
    private String orderField;
    private String orderDirection;
    private String keywords;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    public int getPageSize() {
        return pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    public int getPageStart() {
        return pageNumber > 0 ? (pageNumber - 1) * getPageSize() : 0;
    }
}
