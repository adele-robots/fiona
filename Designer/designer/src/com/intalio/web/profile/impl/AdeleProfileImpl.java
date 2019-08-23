package com.intalio.web.profile.impl;

import javax.servlet.ServletContext;

public class AdeleProfileImpl extends JbpmProfileImpl {

	public AdeleProfileImpl(ServletContext servletContext) {
        this(servletContext, true);
    }
    
    public AdeleProfileImpl(ServletContext servletContext, boolean initializeLocalPlugins) {
        if (initializeLocalPlugins) {
            initializeLocalPlugins(servletContext);
        }
    }
}
