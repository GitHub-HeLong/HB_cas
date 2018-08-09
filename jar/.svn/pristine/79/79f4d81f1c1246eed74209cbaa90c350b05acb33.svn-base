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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.PreventedException;
/*import org.jasig.cas.permission.pojo.AppInfo;
import org.jasig.cas.permission.pojo.UserInfo;
import org.jasig.cas.permission.service.dao.IUserInfoDao;*/
import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.StubPersonAttributeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Resolves principals by querying a data source using the Jasig
 * <a href="http://developer.jasig.org/projects/person-directory/1.5.0-SNAPSHOT/apidocs/">Person Directory API</a>.
 * The {@link org.jasig.cas.authentication.principal.Principal#getAttributes()} are populated by the results of the
 * query and the principal ID may optionally be set by proving an attribute whose first non-null value is used;
 * otherwise the credential ID is used for the principal ID.
 *
 * @author Marvin S. Addison
 * @since 4.0
 *
 */
public class PersonDirectoryPrincipalResolver implements PrincipalResolver {

    /** Log instance. */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean returnNullIfNoAttributes = false;
    
  /*  @NotNull
	private IUserInfoDao iUserInfoDao;
*/
    /** Repository of principal attributes to be retrieved. */
    @NotNull
    private IPersonAttributeDao attributeRepository = new StubPersonAttributeDao(new HashMap<String, List<Object>>());

    /** Optional prinicpal attribute name. */
    private String principalAttributeName;

    
    @NotNull
    private JdbcTemplate jdbcTemplate;
    @NotNull
    private DataSource dataSource;
    
    @Override
    public boolean supports(final Credential credential) {
        return true;
    }
    
    public final void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }
    
    protected final JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    protected final DataSource getDataSource() {
        return this.dataSource;
    }

	/*public IUserInfoDao getiUserInfoDao() {
		return iUserInfoDao;
	}

	public void setiUserInfoDao(IUserInfoDao iUserInfoDao) {
		this.iUserInfoDao = iUserInfoDao;
	}*/
	
	/*@Override
    public final Principal resolve22(final Credential credential) {
        logger.debug("Attempting to resolve a principal...");
        String principalId = extractPrincipalId(credential);   //返回的登录名
        logger.info("=========【输出！！！！！！！！！！】===========principalId="+principalId);
        if (principalId == null) {
            logger.debug("Got null for extracted principal ID; returning null.");
            return null;
        }

        logger.info("Creating SimplePrincipal for [{}]", principalId);
        logger.debug("Creating SimplePrincipal for [{}]", principalId);

        
        //UserInfo userInfo = iUserInfoDao.getUserInfo(principalId, null);
        List<Integer> userType = new ArrayList<Integer>();
        userType.add(1);
        userType.add(2);
        userType.add(3);
        userType.add(4);
        UserInfo userInfo = iUserInfoDao.getUserInfo(principalId, userType);//7.8 修复节点与用户名相同问题 _申
       
        
        final Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
        
        if(userInfo != null){
        	List<Object> userTypes = new ArrayList<Object>();
        	userTypes.add(userInfo.getUserType());
        	attributes.put("userType", userTypes);
        	List<Object> userIds = new ArrayList<Object>();
        	userIds.add(userInfo.getUserId());
        	attributes.put("userId", userIds);
        	List<Object> loginTimes = new ArrayList<Object>();
        	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Date loginTime = userInfo.getLoginTime();
        	System.out.println("loginTime: "+loginTime);
        	if(loginTime!=null)
        	{
        		String strDate=df.format(loginTime);
        	      	 
        		loginTimes.add(strDate);
        		attributes.put("loginTime",loginTimes);
        	}
        	
        	Map<String, AppInfo> rights = userInfo.getRights();
        	if(rights != null && !rights.isEmpty()){
        		Collection<AppInfo> appInfosCollection = rights.values();
        		List<AppInfo> appInfos;
        		List<Object> menuCodes = new ArrayList<Object>();
    			if (appInfosCollection instanceof List)
    				appInfos = (List<AppInfo>)appInfosCollection;
    			else
    				appInfos = new ArrayList<AppInfo>(appInfosCollection);
    			if(appInfos != null && appInfos.size() >0){
    				int len = appInfos.size();
    				for (int i = 0; i < len; i++) {
						AppInfo appInfo = appInfos.get(i);
						if(appInfo != null){
							String menuCode = appInfo.getMenuCode();
							if(menuCode != null && !"".equals(menuCode))
								menuCodes.add(menuCode);
							List<String> menuCodesList = appInfo.getMenuCodes();
							if(menuCodesList != null && menuCodesList.size() > 0){
								menuCodes.addAll(menuCodesList);
							}
						}
					}
    			}
    			attributes.put("menuCodes", menuCodes);
        	}        	
        }
        logger.info("=========【输出！！！！！！！！！！】===========attributes="+attributes);
                
        if (attributes == null & !this.returnNullIfNoAttributes) {
            return new SimplePrincipal(principalId);
        }

        if (attributes == null) {
            return null;
        }

        final Map<String, Object> convertedAttributes = new HashMap<String, Object>();
        for (final String key : attributes.keySet()) {
            final List<Object> values = attributes.get(key);
            if (key.equalsIgnoreCase(this.principalAttributeName)) {
                if (values.isEmpty()) {
                    logger.debug("{} is empty, using {} for principal", this.principalAttributeName, principalId);
                } else {
                    principalId = values.get(0).toString();
                    logger.debug(
                            "Found principal attribute value {}; removing {} from attribute map.",
                            principalId,
                            this.principalAttributeName);
                }
            } else {
                convertedAttributes.put(key, values.size() == 1 ? values.get(0) : values);
            }
        }
        
        logger.info("=========【输出！！！！！！！！！！】===========principalId="+principalId+",convertedAttributes="+convertedAttributes+",principalAttributeName="+this.principalAttributeName);
        return new SimplePrincipal(principalId, convertedAttributes);
    }*/
    
    @Override
    public final Principal resolve(final Credential credential) {
        logger.debug("Attempting to resolve a principal...");
        String principalId = extractPrincipalId(credential);   //返回的登录名
        logger.info("【server-core】。。。。。。。。。principalId={}", principalId);
        logger.debug("**********【debug】***************");
        logger.error("**********【error】***************");
        if (principalId == null) {
            logger.debug("Got null for extracted principal ID; returning null.");
            return null;
        }

        logger.debug("Creating SimplePrincipal for [{}]", principalId);
        
        
        final Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
        List<Object> userTypes = new ArrayList<Object>();
        List<Object> userIds = new ArrayList<Object>();
    	List<Object> loginTimes = new ArrayList<Object>();
    	List<Object> menuCodes = new ArrayList<Object>();
    	List<Object> roleIds = new ArrayList<Object>();
    	List<Object> areaIds = new ArrayList<Object>();
        
        String querySQL = "SELECT userType FROM imm_userinfo,imm_assemble_cfg WHERE userAccount = '"+principalId+"' AND platformId = imm_assemble_cfg.platform_id AND imm_assemble_cfg.platform_name='本平台'";
        try {    	
            final String userType = getJdbcTemplate().queryForObject(querySQL, String.class);
            userTypes.add(userType);
        }catch(DataAccessException e){
        	logger.error("--*【err】*--  querySQL={},e={}",querySQL,e.toString());
		}
    	attributes.put("userType", userTypes);
    	
    	querySQL = "SELECT userId FROM imm_userinfo,imm_assemble_cfg WHERE userAccount = '"+principalId+"' AND platformId = imm_assemble_cfg.platform_id AND imm_assemble_cfg.platform_name='本平台'";
        try {    	
            final String userId = getJdbcTemplate().queryForObject(querySQL, String.class);
            userIds.add(userId);
        }catch(DataAccessException e){
        	logger.error("--*【err】*--  querySQL={},e={}",querySQL,e.toString());
		}
    	attributes.put("userId", userIds);
    	
/*---------------------    	*/
    	querySQL = "SELECT areaId FROM imm_userinfo,imm_assemble_cfg WHERE userAccount = '"+principalId+"' AND platformId = imm_assemble_cfg.platform_id AND imm_assemble_cfg.platform_name='本平台'";
        try {    	
            final String areaId = getJdbcTemplate().queryForObject(querySQL, String.class);
            areaIds.add(areaId);
        }catch(DataAccessException e){
        	logger.error("--*【err】*--  querySQL={},e={}",querySQL,e.toString());
		}
    	attributes.put("areaId", areaIds);
    	
/*----------设置用户应用服务-----------   */
    	
    	
    	
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date loginTime = new Date();
    	System.out.println("loginTime: "+loginTime);
		String strDate=df.format(loginTime);     	 
		loginTimes.add(strDate);
		attributes.put("loginTime",loginTimes);	
		
		String roleId = "";
		querySQL = "SELECT roleId FROM imm_userinfo,imm_assemble_cfg WHERE userAccount = '"+principalId+"' AND platformId = imm_assemble_cfg.platform_id AND imm_assemble_cfg.platform_name='本平台'";
        try {    	
            roleId = getJdbcTemplate().queryForObject(querySQL, String.class);
        }catch(DataAccessException e){
        	logger.error("--*【err】*--  querySQL={},e={}",querySQL,e.toString());
        	roleId = "";
		}
        roleIds.add(roleId);
        attributes.put("roleId", roleIds);
        if(!"".equals(roleId)){	
    		querySQL = "SELECT * FROM imm_roleapp WHERE roleId =  '"+roleId+"'";
    		try{
    			List<Map<String, Object>> total_list = jdbcTemplate.queryForList(querySQL);
    			if(total_list != null){
    				int total = total_list.size();
    				for(int i = 0; i<total; i++){
    					Map<String, Object> map = total_list.get(i);
    					menuCodes.add(map.get("applicationId"));				
    				}
    			}
    		}catch(DataAccessException e){
    			logger.error("--*【err】*--  querySQL={},e={}",querySQL,e.toString());
    		}
    		attributes.put("menuCodes", menuCodes);
        }
                    
        if (attributes == null & !this.returnNullIfNoAttributes) {
            return new SimplePrincipal(principalId);
        }

        if (attributes == null) {
            return null;
        }
        final Map<String, Object> convertedAttributes = new HashMap<String, Object>();
        for (final String key : attributes.keySet()) {
            final List<Object> values = attributes.get(key);
            if (key.equalsIgnoreCase(this.principalAttributeName)) {
                if (values.isEmpty()) {
                    logger.debug("{} is empty, using {} for principal", this.principalAttributeName, principalId);
                } else {
                    principalId = values.get(0).toString();
                    logger.debug(
                            "Found principal attribute value {}; removing {} from attribute map.",
                            principalId,
                            this.principalAttributeName);
                }
            } else {
                convertedAttributes.put(key, values.size() == 1 ? values.get(0) : values);
            }
        }
        logger.info("【server-core】。。。。。。。。。principalId={},convertedAttributes={}", principalId,convertedAttributes);
        return new SimplePrincipal(principalId, convertedAttributes);
    }
    
    /*
     * 将mysql改为mongodb后，为获取用户其他信息做了修改
     * 此函数为修改前函数
     * @Override*/
	//@Override
    public final Principal resolveOld(final Credential credential) {
        logger.debug("Attempting to resolve a principal...");
        String principalId = extractPrincipalId(credential);   //返回的登录名
        if (principalId == null) {
            logger.debug("Got null for extracted principal ID; returning null.");
            return null;
        }

        logger.debug("Creating SimplePrincipal for [{}]", principalId);

      
        final IPersonAttributes personAttributes = this.attributeRepository.getPerson(principalId);
        final Map<String, List<Object>> attributes;

        if (personAttributes == null) {
            attributes = null;
        } else {
            attributes = personAttributes.getAttributes();
        }

        if (attributes == null & !this.returnNullIfNoAttributes) {
            return new SimplePrincipal(principalId);
        }

        if (attributes == null) {
            return null;
        }
        final Map<String, Object> convertedAttributes = new HashMap<String, Object>();
        for (final String key : attributes.keySet()) {
            final List<Object> values = attributes.get(key);
            if (key.equalsIgnoreCase(this.principalAttributeName)) {
                if (values.isEmpty()) {
                    logger.debug("{} is empty, using {} for principal", this.principalAttributeName, principalId);
                } else {
                    principalId = values.get(0).toString();
                    logger.debug(
                            "Found principal attribute value {}; removing {} from attribute map.",
                            principalId,
                            this.principalAttributeName);
                }
            } else {
                convertedAttributes.put(key, values.size() == 1 ? values.get(0) : values);
            }
        }
        return new SimplePrincipal(principalId, convertedAttributes);
    }

    public final void setAttributeRepository(final IPersonAttributeDao attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public void setReturnNullIfNoAttributes(final boolean returnNullIfNoAttributes) {
        this.returnNullIfNoAttributes = returnNullIfNoAttributes;
    }
    
	/**
     * Sets the name of the attribute whose first non-null value should be used for the principal ID.
     *
     * @param attribute Name of attribute containing principal ID.
     */
    public void setPrincipalAttributeName(final String attribute) {
        this.principalAttributeName = attribute;
    }

    /**
     * Extracts the id of the user from the provided credential. This method should be overridded by subclasses to
     * achieve more sophisticated strategies for producing a principal ID from a credential.
     *
     * @param credential the credential provided by the user.
     * @return the username, or null if it could not be resolved.
     */
    protected String extractPrincipalId(final Credential credential) {
        return credential.getId();
    }
}
