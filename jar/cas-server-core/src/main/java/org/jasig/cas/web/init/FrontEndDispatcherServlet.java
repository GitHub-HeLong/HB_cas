package org.jasig.cas.web.init;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

public final class FrontEndDispatcherServlet extends HttpServlet{
	
	 /** The actual DispatcherServlet to which we will delegate to. */
    private DispatcherServlet delegate = new DispatcherServlet();
    
    private static final Logger logger = LoggerFactory.getLogger(SafeDispatcherServlet.class);
    
    public void init(final ServletConfig config) {
    	logger.info("FrontEndDispatcherServlet start.");
    	try {
			this.delegate.init(config);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void service(final ServletRequest req, final ServletResponse resp) throws ServletException, IOException {
    	logger.info("front end request start.");
    	this.delegate.service(req, resp);
    }

}
