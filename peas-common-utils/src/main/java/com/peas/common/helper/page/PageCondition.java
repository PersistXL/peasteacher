package com.peas.common.helper.page;

import lombok.Data;

/**
 * 分页查询条件
 *
 * @author zhc 2015-2-4
 */
@Data
public class PageCondition {
    /**
     * 查询字段名称
     */
    private String column;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 操作类型(> 、 < 、>=、in)
     */
    private String op;

    /**
     * 字段值
     */
    private String value;

}
