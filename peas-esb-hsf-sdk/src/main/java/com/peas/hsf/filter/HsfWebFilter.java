package com.peas.hsf.filter;

import com.peas.hsf.HsfContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author duanyihui
 * @Date 2016/3/18
 * @Description
 */
public class HsfWebFilter implements Filter {
    /**
     * Called by the web container to indicate to a filter that it is
     * being placed into service.
     * <p>
     * <p>The servlet container calls the init
     * method exactly once after instantiating the filter. The init
     * method must complete successfully before the filter is asked to do any
     * filtering work.
     * <p>
     * <p>The web container cannot place the filter into service if the init
     * method either
     * <ol>
     * <li>Throws a ServletException
     * <li>Does not return within a time period defined by the web container
     * </ol>
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public HsfWebFilter() {
        super();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        HttpServletRequest request1 = (HttpServletRequest) request;
        HsfContext.addHttpSession(request1.getSession());
        HsfContext.addHttpRequest(request1);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
