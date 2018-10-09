package com.peas.hsf.http;

import javax.ws.rs.core.NewCookie;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 客户端接口
 *
 * @author dyh 2015年2月3日
 * @see
 * @since 1.0
 */
public interface Client extends Authentication, MediaTypeHandler
{

    /**
     * 请求资源
     *
     * @param resource
     * @param argument
     * @return
     */
    Object post(String resource, Object argument);

    /**
     * 更新资源 （幂等）
     *
     * @param resource
     * @param argument
     * @return
     */
    Object put(String resource, Object argument);

    /**
     * 获取资源
     *
     * @param resource
     * @return
     */
    Object get(String resource);

    /**
     * 删除资源
     *
     * @param resource
     * @return
     */
    Object delete(String resource);

    /**
     * 请求资源
     *
     * @param resource
     * @param argument
     * @return
     */
    void asyncPost(String resource, Object argument, final Callback callback);

    /**
     * 更新资源 （幂等）
     *
     * @param resource
     * @param argument
     * @return
     */
    void asyncPut(String resource, Object argument, final Callback callback);

    /**
     * 获取资源
     *
     * @param resource
     * @return
     */
    void asyncGet(String resource, final Callback callback);

    /**
     * 删除资源
     *
     * @param resource
     * @return
     */
    void asyncDelete(String resource, final Callback callback);

    /**
     * 注册
     */
    void register(Class<?> clz);

    Client checkResponse();

    Client header(String head, Object value);

    Client cookie(String name, String value);

    Client cookie(Collection<NewCookie> cookies);

    Client cookie(NewCookie cookies);

    Client callCookie(Consumer<Map<String, NewCookie>> consumer);


    Client onThrowable(Consumer<Throwable> consumer);

    Client returnResponse();

}
