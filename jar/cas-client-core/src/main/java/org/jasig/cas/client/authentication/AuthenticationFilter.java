/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.client.authentication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Assertion;

/**
 * Filter implementation to intercept all requests and attempt to authenticate
 * the user by redirecting them to CAS (unless the user has a ticket).
 * <p>
 * This filter allows you to specify the following parameters (at either the context-level or the filter-level):
 * <ul>
 * <li><code>casServerLoginUrl</code> - the url to log into CAS, i.e. https://cas.rutgers.edu/login</li>
 * <li><code>renew</code> - true/false on whether to use renew or not.</li>
 * <li><code>gateway</code> - true/false on whether to use gateway or not.</li>
 * </ul>
 *
 * <p>Please see AbstractCasFilter for additional properties.</p>
 *
 * @author Scott Battaglia
 * @author Misagh Moayyed
 * @since 3.0
 */
public class AuthenticationFilter extends AbstractCasFilter {
    /**
     * The URL to the CAS Server login.
     */
    private String casServerLoginUrl;

    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;
    
    private String userType;
    
    private String authorizationFailure;
    
    private String sysType;
    
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;

    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

    private AuthenticationRedirectStrategy authenticationRedirectStrategy = new DefaultAuthenticationRedirectStrategy();
    
    private UrlPatternMatcherStrategy ignoreUrlPatternMatcherStrategyClass = null;
    
    private static final Map<String, Class<? extends UrlPatternMatcherStrategy>> PATTERN_MATCHER_TYPES =
            new HashMap<String, Class<? extends UrlPatternMatcherStrategy>>();
    
    static {
        PATTERN_MATCHER_TYPES.put("CONTAINS", ContainsPatternUrlPatternMatcherStrategy.class);
        PATTERN_MATCHER_TYPES.put("REGEX", RegexUrlPatternMatcherStrategy.class);
        PATTERN_MATCHER_TYPES.put("EXACT", ExactUrlPatternMatcherStrategy.class);
    }
    
    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
            logger.trace("Loaded CasServerLoginUrl parameter: {}", this.casServerLoginUrl);
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            logger.trace("Loaded renew parameter: {}", this.renew);
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
            logger.trace("Loaded gateway parameter: {}", this.gateway);
              
            setUserType(getPropertyFromInitParams(filterConfig, "userType", "1"));
            
            setAuthorizationFailure(getPropertyFromInitParams(filterConfig,"authorizationFailure",""));
            
            setSysType(getPropertyFromInitParams(filterConfig,"sysType",""));
            
            final String ignorePattern = getPropertyFromInitParams(filterConfig, "ignorePattern", null);
            logger.trace("Loaded ignorePattern parameter: {}", ignorePattern);
            
            final String ignoreUrlPatternType = getPropertyFromInitParams(filterConfig, "ignoreUrlPatternType", "REGEX");
            logger.trace("Loaded ignoreUrlPatternType parameter: {}", ignoreUrlPatternType);
            
            if (ignorePattern != null) {
                final Class<? extends UrlPatternMatcherStrategy> ignoreUrlMatcherClass = PATTERN_MATCHER_TYPES.get(ignoreUrlPatternType);
                if (ignoreUrlMatcherClass != null) {
                    this.ignoreUrlPatternMatcherStrategyClass = ReflectUtils.newInstance(ignoreUrlMatcherClass.getName());
                } else {
                    try {
                        logger.trace("Assuming {} is a qualified class name...", ignoreUrlPatternType);
                        this.ignoreUrlPatternMatcherStrategyClass = ReflectUtils.newInstance(ignoreUrlPatternType);
                    } catch (final IllegalArgumentException e) {
                        logger.error("Could not instantiate class [{}]", ignoreUrlPatternType, e);
                    }
                }
                if (this.ignoreUrlPatternMatcherStrategyClass != null) {
                    this.ignoreUrlPatternMatcherStrategyClass.setPattern(ignorePattern);
                }
            }
            
            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

            if (gatewayStorageClass != null) {
                this.gatewayStorage = ReflectUtils.newInstance(gatewayStorageClass);
            }
            
            final String authenticationRedirectStrategyClass = getPropertyFromInitParams(filterConfig,
                    "authenticationRedirectStrategyClass", null);

            if (authenticationRedirectStrategyClass != null) {
                this.authenticationRedirectStrategy = ReflectUtils.newInstance(authenticationRedirectStrategyClass);
            }
        }
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
    }
    
    public String rebuildUrl(HttpServletRequest request){
    	String requestURI = request.getRequestURI();
    	Map parameters = request.getParameterMap();
    	StringBuffer originalURL = new StringBuffer(requestURI);
		originalURL.append("?");
		if (parameters != null && parameters.size() > 0) {
            for (Iterator iter = parameters.keySet().iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                if(key.equals("ticket"))
                	continue;
                String[] values = (String[]) parameters.get(key);
                for (int i = 0; i < values.length; i++) {
                    originalURL.append(key).append("=").append(values[i]).append("&");
                }
            }
        }
		originalURL.append("htmlrandom=").append(System.currentTimeMillis());
    	return originalURL.toString();
    }
    
    /**
     *用户密码及ticket校验通过之后， 校验用户是否有权限进入系统
     * @param assertion
     * @return 0 表示无权限，1 表示有权限
     */
    public Integer verifyPermission(Assertion assertion){
    /*	if(userType == null){
            return 1;//如果系统没有配置能进入该系统的用户类型，则表示该系统任何用户均可进入
		}*/
    	if(authorizationFailure == null || "".equals(authorizationFailure)){
    		return 1;
    	}
		AttributePrincipal principal = assertion.getPrincipal();   //server resolve 传来的   返回的登录名
    	Map<String, Object> attributes = principal.getAttributes();    //server resolve 传来的convertedAttributes
    	logger.info("【client-core】 principal={},attributes={}",principal,attributes);
		//Object loginUserType = attributes.get("userType");
		/*if(loginUserType != null){
			log.info("[loginUserType:]  " + loginUserType.toString());
			int parseInt = Integer.parseInt(loginUserType.toString());
			if(Integer.parseInt(userType) == parseInt){*/
				if(this.sysType != null && !"".equals(this.sysType)){
					Object menuCodes = attributes.get("menuCodes");	//用户可进入的所有系统的系统类型
					if(menuCodes != null){
						String menuCodeString = menuCodes.toString();
						if(menuCodeString != null){
							menuCodeString = menuCodeString.replaceAll("[\\[\\]]", "");
							menuCodeString = menuCodeString.replaceAll(" ", "");
							try {
								String[] split = menuCodeString.split(",");
								if(split != null && split.length > 0){
									List<String> asList = Arrays.asList(split);
									if(asList != null && asList.contains(this.sysType)){
							            return 1;//用户拥有此系统的权限
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								logger.info("verify permission exception : {}",e.toString());
							}
						}
					}
					logger.info("【client-core】 无权限");
					return 0;//用户没有任何权限
				}
	            return 1;
			/*}
		}
		return 0;  */      
    }
    
    public String rebuildFailureUrl(HttpServletRequest request){
    	StringBuilder authorizationFailure = new StringBuilder(this.authorizationFailure);
		if(this.authorizationFailure != null && !"".equals(this.authorizationFailure)){
			authorizationFailure.append("?logoutUrl=");
			authorizationFailure.append(this.casServerLoginUrl.replace("login", "logout"));
			authorizationFailure.append("?service=");
			String url = String.format("%s://%s:%s%s", request.getScheme(),request.getServerName(),request.getServerPort(),request.getRequestURI());
			authorizationFailure.append(url);
		}
		return authorizationFailure.toString();
    }
    
    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
       
        if (isRequestUrlExcluded(request)) {
            log.info("Request is ignored.");
            filterChain.doFilter(request, response);
            return;
        }
        
        final HttpSession session = request.getSession(false);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;
        //String newTicket = session != null ? (String) session.getAttribute(CONST_CAS_TICKET) : null;
        //log.info("the request's ticket is "+newTicket);
        if (assertion != null) {
        	String requestURI = request.getRequestURI();
        	Map parameters = request.getParameterMap();
        	boolean addParam = requestURI.contains(".html")&&!parameters.containsKey("htmlrandom");
        	String originalURL = requestURI;
        	if(addParam){
        		originalURL =this.rebuildUrl(request);
        	}
        	
        	Integer verifyPermission = this.verifyPermission(assertion);
        	switch (verifyPermission) {
				case 0:
		    		response.sendRedirect(this.rebuildFailureUrl(request));
					request.getSession().invalidate();
					break;
				case 1:
					if(addParam){
						response.sendRedirect(originalURL);
					}
					else {
						filterChain.doFilter(request, response);
					}
					break;
			}
        	return;
        }

        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = retrieveTicketFromRequest(request);
        String ticketParameter = request.getParameter("ticket");
        log.info("the parameter is: " + ticketParameter);
        log.info("the ticket is: " + ticket);
        final boolean wasGatewayed = this.gateway && this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
        	log.info("wasGatewayed: " + wasGatewayed);
            filterChain.doFilter(request, response);
            return;
        }

        /*if(CommonUtils.isNotBlank(ticketParameter)){
        	filterChain.doFilter(request, response);
        	return;
        }*/
        
        final String modifiedServiceUrl;

        logger.debug("no ticket and no assertion found");
        if (this.gateway) {
            logger.debug("setting gateway attribute in session");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }

        logger.info("Constructed service url: {}", modifiedServiceUrl);
        logger.debug("Constructed service url: {}", modifiedServiceUrl);

        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl,
                getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);
        
        log.info("urlToRedirectTo: " + urlToRedirectTo);
        logger.debug("redirecting to \"{}\"", urlToRedirectTo);
        this.authenticationRedirectStrategy.redirect(request, response, urlToRedirectTo,this.casServerLoginUrl);
    }

    public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(final boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerLoginUrl(final String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
        this.gatewayStorage = gatewayStorage;
    }
        
    private boolean isRequestUrlExcluded(final HttpServletRequest request) {
        if (this.ignoreUrlPatternMatcherStrategyClass == null) {
            return false;
        }
        
        final StringBuffer urlBuffer = request.getRequestURL();
        if (request.getQueryString() != null) {
            urlBuffer.append("?").append(request.getQueryString());
        }
        final String requestUri = urlBuffer.toString();
        return this.ignoreUrlPatternMatcherStrategyClass.matches(requestUri);
    }

	public final String getUserType() {
		return userType;
	}

	public final void setUserType(String userType) {
		log.info("setUserType "+userType+".");
		this.userType = userType;
	}

	public String getAuthorizationFailure() {
		return authorizationFailure;
	}

	public void setAuthorizationFailure(String authorizationFailure) {
		this.authorizationFailure = authorizationFailure;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
}
