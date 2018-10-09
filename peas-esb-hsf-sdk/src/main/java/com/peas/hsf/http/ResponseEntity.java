package com.peas.hsf.http;

import lombok.Data;

/**
 * 返回结果实体
 *
 * @Author duanyihui
 * @Date 2016/3/29
 * @Description
 */
@Data
public class ResponseEntity {

    /**
     * 业务号码
     */
    public int code;

    /**
     * 返回信息
     */
    public String message;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 业务数据
     */
    private Object data;


    private String $type = "json";

}
