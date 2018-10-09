package com.peas.common.constant;

import com.google.common.base.Enums;
import com.google.common.base.Optional;

/**
 * redis 数据库枚举持有者
 *
 * @Description
 *
 * @Author He Enqi
 * @Date 2015年11月19日 上午10:43:47
 *
 */
public enum RedisDBHolder
{
    TEMP_DATA_DATABASE, DCM_DATA_DATABASE, DCM_LIST_DATABASE, NFS_DATA_DATABASE, URL_DATA_DATABASE;

    public static RedisDBHolder StringToRedisDBHolder(String name)
    {
        Optional<RedisDBHolder> o = Enums.getIfPresent(RedisDBHolder.class, name);
        return o.orNull();
    }

}
