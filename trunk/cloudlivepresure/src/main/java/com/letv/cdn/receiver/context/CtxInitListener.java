package com.letv.cdn.receiver.context;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 应用上下文
 *
 * @author liufeng1
 * @date 14/4/2015
 */
public class CtxInitListener implements ServletContextListener {

    private static ApplicationContext ctx;

    public static ApplicationContext getCTX(){
        return ctx;
    }

    public void contextInitialized(ServletContextEvent sce) {
        this.ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
