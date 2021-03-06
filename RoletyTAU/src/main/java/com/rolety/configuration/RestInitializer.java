package com.rolety.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class RestInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext ServletContext) throws ServletException {
        WebApplicationContext context = getContext();
        ServletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = ServletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
    }

    private AnnotationConfigWebApplicationContext getContext()
    {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.rolety.configuration");
        return context;
    }
}