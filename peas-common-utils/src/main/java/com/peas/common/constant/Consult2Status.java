package com.peas.common.constant;

/**
 * Created by duan on 2015/12/29.
 */
public interface Consult2Status {

    /**
     * 已提交
     */
    int SUBMITTED = 0;

    /**
     * 已审核
     */
    int APPROVED = 1;

    /**
     * 已回复
     */
    int REPLIED = 2;


    /**
     * 同步中
     */
    int SYNCHRONIZING = 3;


    /**
     * 已关闭
     */
    int CLOSED = 4;

}
