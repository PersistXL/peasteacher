package com.peas.hsf.http;

/**
 * @Author duanyihui
 * @Date 2016/3/29
 * @Description
 */
public interface ErrorCode {

    /* 编码规则 : 采用类似Http static code 进行编码
       编码数字为四位数字
       首位为Http编码的开头位
       中间为两位为业务代号,末尾为代码意义
    */

    /**
     * 成功
     */
    int SUCCESS = 200;

    /**
     * 未登录
     */
    int UN_LOGIN = 4101;

    /**
     * 已经注册
     */
    int HAS_REGISTED = 4102;

    /**
     * 服务器错误
     */
    int ERROR = 5000;

    /**
     * 校验错误
     */
    int VERIFY_ERROR = 5001;

    /**
     * 工作流程任务重复申请
     */
    int WORK_FLOW_APPLY_REPEAT = 5101;
    /**
     * 医生团队名称重复
     */
    int DORTOR_TEAM_REPEAT = 5201;

    /**
     * 客户已存在
     */
    int CUSTOMER_REAPEAT = 5301;
    /**
     * 未找到
     */
    int NOT_FOUND = 4004;

}
