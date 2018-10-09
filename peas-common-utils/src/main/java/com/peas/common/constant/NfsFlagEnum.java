package com.peas.common.constant;

/**
 * 文件服务标志枚举
 *
 * @Description
 * @Author He Enqi
 * @Date 2015年9月28日 上午10:38:04
 */
public enum NfsFlagEnum
{

    // 上传状态类型
    /**
     * 秒传
     *
     * @Description 上传状态类型
     */
    SEC_TRANSFER,

    /**
     * 上传
     *
     * @Description 上传状态类型
     */
    UPLOADING,

    /**
     *
     *
     * @Description 上传状态类型
     */
    START,
    /**
     * 上传完成
     *
     * @Description 上传状态类型
     */
    FINISH,

    // 上传任务类型
    /**
     * 远程咨询
     *
     * @Description 上传任务类型
     */
    REMOTE_CONSULT,

    /**
     * 标准
     *
     * @Description 上传任务类型
     */
    STANDARD;

    public static NfsFlagEnum fromName(String name)
    {
        NfsFlagEnum[] enums = values();
        for (NfsFlagEnum e : enums)
        {
            if (e.name().equals(name))
            {
                return e;
            }
        }
        return null;
    }
}
