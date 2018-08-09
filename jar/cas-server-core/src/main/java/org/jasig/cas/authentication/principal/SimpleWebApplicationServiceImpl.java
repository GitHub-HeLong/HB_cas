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
package org.jasig.cas.authentication.principal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.principal.Response.ResponseType;
import org.springframework.util.StringUtils;

/**
 * Represents a service which wishes to use the CAS protocol.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
public final class SimpleWebApplicationServiceImpl extends AbstractWebApplicationService {

    private static final String CONST_PARAM_SERVICE = "service";

    private static final String CONST_PARAM_TARGET_SERVICE = "targetService";

    private static final String CONST_PARAM_TICKET = "ticket";

    private static final String CONST_PARAM_METHOD = "method";
    
    private static final String CONST_PARAM_CLIENT_IP = "clientIp";
    
    private static final String CONST_PARAM_SYS_TYPE = "sysType";

    private final ResponseType responseType;

    private static final long serialVersionUID = 8334068957483758042L;

    public SimpleWebApplicationServiceImpl(final String id) {
    	this(id, id, null, null, null,null);
    }

    private SimpleWebApplicationServiceImpl(final String id,
            final String originalUrl, final String artifactId,
            final ResponseType responseType,final String clientIp,
            final String sysType) {
            super(id, originalUrl, artifactId,clientIp,sysType);
            this.responseType = responseType;
    }

    public static SimpleWebApplicationServiceImpl createServiceFrom(
        final HttpServletRequest request) {
        final String targetService = request
            .getParameter(CONST_PARAM_TARGET_SERVICE);
        final String method = request.getParameter(CONST_PARAM_METHOD);
        final String serviceToUse = StringUtils.hasText(targetService)
            ? targetService : request.getParameter(CONST_PARAM_SERVICE);

        String clientIp = request.getParameter(CONST_PARAM_CLIENT_IP);
        
        String sysType = request.getParameter(CONST_PARAM_SYS_TYPE);
        
        if (!StringUtils.hasText(serviceToUse)) {
            return null;
        }

        final String id = cleanupUrl(serviceToUse);
        final String artifactId = request.getParameter(CONST_PARAM_TICKET);
        return new SimpleWebApplicationServiceImpl(id, serviceToUse,
                artifactId, "POST".equals(method) ? ResponseType.POST
                        : ResponseType.REDIRECT,clientIp,sysType);
    }

    public Response getResponse(final String ticketId) {
        final Map<String, String> parameters = new HashMap<String, String>();

        if (StringUtils.hasText(ticketId)) {
            parameters.put(CONST_PARAM_TICKET, ticketId);
        }

        if (ResponseType.POST == this.responseType) {
            return Response.getPostResponse(getOriginalUrl(), parameters);
        }
        return Response.getRedirectResponse(getOriginalUrl(), parameters);
    }
}