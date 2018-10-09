package com.peas.common.constant;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件服务消息枚举
 *
 * @Description
 * @Author He Enqi
 * @Date 2015年9月28日 上午10:37:24
 */
public enum NfsMessageEnum
{
    /**
     * 任务Id
     */
    TASKTOKEN,

    /**
     * 任务状态标志
     */
    FLAG,

    /**
     * 上传类型
     */
    UPLOADTYPE,

    /**
     * 一组文件的MD5
     */
    MD5S,

    /**
     * 单个文件的MD5
     */
    FILEMD5VALUE,

    /**
     * 文件名
     */
    FILENAME,

    /**
     * 文件类型
     */
    FILETYPE,

    /**
     * 任务类型
     */
    TASKTYPE,
    /**
     * 任务消息
     */
    TASKMESSAGE,

    /**
     * 文件大小
     */
    SIZE,

    /**
     * 错误码
     */
    code,

    /**
     * 错误信息
     */
    message,

    /**
     * 文件路径 低配版opc专用
     */
    PATH,

    UNKNOWN;

    public static HashMap<NfsMessageEnum, Object> newMessageMap()
    {
        return Maps.newHashMap();
    }

    public static HashMap<NfsMessageEnum, Object> toEnumMessageMap(Map<String, Object> map)
    {
        HashMap<NfsMessageEnum, Object> rult = Maps.newHashMap();
        map.forEach((key, value) -> rult.put(NfsMessageEnum.valueOf(key), map.get(key)));
        return rult;
    }
}
