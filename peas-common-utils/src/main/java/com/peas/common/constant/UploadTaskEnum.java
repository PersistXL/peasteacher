package com.peas.common.constant;

/**
 * 上传任务结果枚举
 *
 * @Description
 *
 * @Author He Enqi
 * @Date 2015年9月28日 上午10:10:40
 *
 */
public enum UploadTaskEnum
{
    /**
     * 正在上传
     */
    UPLOADING,

    /**
     * 错误
     */
    ERROR,

    /**
     * 影像上传完成
     */
    DCM_UPLOADED,

    /**
     * 图片上传完成
     */
    IMAGE_UPLOADED;
}
