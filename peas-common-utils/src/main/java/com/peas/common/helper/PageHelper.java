package com.peas.common.helper;

import com.peas.common.helper.page.AttributeUtils;
import com.peas.common.helper.page.PageCondition;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 *
 * @author zhc 2015-4-17
 */
public class PageHelper {
    /**
     * 当前页
     */
    private int page;

    /**
     * 每页记录数
     */
    private int rows;

    /**
     * 分页数据
     */
    private List<Object> data;

    /**
     * 总记录数
     */
    private int records;

    /**
     * 总页数
     */
    private int total;

    /**
     * 排序字段名称
     */
    private String sortName;

    /**
     * 排序规则
     */
    private String sortOrder;

    /**
     * 搜索参数
     */
    private String jsonParams;

    private List<PageCondition> params;

    public PageHelper() {
    }

    public PageHelper(int page, int rows, String sortname, String sortorder, String jsonParams) {
        super();
        this.page = page;
        this.rows = rows;
        this.sortName = sortname;
        this.sortOrder = sortorder;
        this.jsonParams = jsonParams;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getFirstIndex() {
        if (page < 1) {
            page = 1;
        }
        return (page - 1) * rows;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
        this.total = records % rows == 0 ? records / rows : (records / rows + 1);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * 搜索条件json数组拼凑
     *
     * @return
     */
    public Map<String, Object> getParams() {
        castParam();
        Map<String, Object> result = new HashMap<String, Object>();
        if (jsonParams == null) {
            return result;
        }
        StringBuffer sql = new StringBuffer();
        int paramIndex = 0;
        String paramName = null, diff = null;// 占位符
        for (PageCondition f : params) {
            String column = AttributeUtils.attribute2column(f.getColumn());
            String type = f.getType();
            String op = f.getOp();
            String value = f.getValue();
            // sql
            sql.append(" and ");
            sql.append(column);
            sql.append(" ");
            sql.append(op);
            sql.append(" ");

            if ("in".equals(op)) {
                String[] values = value.split(",");
                sql.append(" (");
                for (int i = 0; i < values.length; i++) {
                    paramName = "param" + (paramIndex++);
                    diff = "#{" + paramName + "}";
                    result.put(paramName, values[i]);
                    if (i > 0) {
                        sql.append(",");
                    }
                    sql.append(diff);
                }
                sql.append(") ");
            } else {
                paramName = "param" + (paramIndex++);
                diff = "#{" + paramName + "}";
                if ("string".equals(type)) {
                    sql.append(diff);
                } else if ("number".equals(type)) {
                    sql.append(diff);
                } else if ("date".equals(type)) {
                    sql.append("str_to_date(");
                    sql.append(diff);
                    sql.append(",'%Y-%m-%d')");
                }
                // param
                if ("string".equals(type)) {
                    result.put(paramName, "like".equals(op) ? ("%" + value + "%") : value);
                } else if ("number".equals(type)) {
                    result.put(paramName, Integer.parseInt(value));
                } else if ("date".equals(type)) {
                    result.put(paramName, value);
                }
            }
        }
        result.put("filter", sql.toString());
        return result;
    }

    public String getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(String jsonParams) {
        this.jsonParams = jsonParams;
    }

    private void castParam() {
        if (params != null) {
            return;
        }
        JSONArray array = JSONArray.fromObject(jsonParams);
        params = new ArrayList<PageCondition>();
        for (int i = 0; i < array.size(); i++) {
            params.add((PageCondition) JSONObject.toBean(array.getJSONObject(i), PageCondition.class));
        }
    }
}
