package org.jasig.cas.adaptors.mongo;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class QueryMongoDatabaseAuthenticationHandler extends AbstractMongoUsernamePasswordAuthenticationHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential)
			throws GeneralSecurityException, PreventedException {
		final String username = credential.getUsername();
	    final String encryptedPassword = this.getPasswordEncoder().encode(credential.getPassword());
	    logger.info("=========【输出！！！！！！！！！！】===========username="+username+",encryptedPassword="+encryptedPassword);
	    logger.info("encryptedPassword is {}",encryptedPassword);
	    Criteria criteria = new Criteria();
    	criteria.andOperator(Criteria.where(getUsernameField()).is(username),Criteria.where("userType").ne(-1));
    	//Query query = Query.query(Criteria.where(getUsernameField()).is(username));
    	Query query = Query.query(criteria);
	    try {
	    	logger.info("the collection is {},usernameField is {},passwordField is {}.",
	    			getUserCollection(),getUsernameField(),getPasswordField());
	    	
		    BasicDBObject projection = new BasicDBObject(getPasswordField(),1);
		    DBObject dbObject = getMongoTemplate().getCollection(getUserCollection()).findOne(query.getQueryObject(), projection);
	    	if(null != dbObject || "".equals(dbObject)){
	    		String passWord = (String) dbObject.get("password");
	    		logger.info("the password is {}",passWord);
	    		if(!passWord.equals(encryptedPassword)) {
	    			logger.info("Password does not match value on record.");
	                throw new FailedLoginException("Password does not match value on record.");
	            }
	    	}
	    	else {
	    		String superAdmin = this.getSuperAdministrator();
	    		if(superAdmin.equals(username)){
	    			String passWord = this.getSuperPassword();
	    			Map<String, Object> userInfo = new HashMap<String, Object>();
	    			userInfo.put(this.getUsernameField(), superAdmin);
	    			userInfo.put(this.getPasswordField(), this.getPasswordEncoder().encode(passWord));
	    			userInfo.put("userType", 4);
	    			userInfo.put("parentId", "0");
	    		    getMongoTemplate().insert(userInfo, getUserCollection());
	    		    if(!passWord.equals(credential.getPassword())){
	    		    	throw new FailedLoginException("Password does not match value on record.");
	    		    }
	    		}
	    		else {
	    			throw new AccountNotFoundException(username + " not found.");
				}
			}
            
        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                throw new AccountNotFoundException(username + " not found with SQL query");
            } else {
                throw new FailedLoginException("Multiple records found for " + username);
            }
        } catch (final DataAccessException e) {
            throw new PreventedException("SQL exception while executing query for " + username, e);
        }
	    try {
	    	Update update = new Update();
		    update.set(this.getLoginTimeField(), new Date());
		    getMongoTemplate().updateFirst(query, update, getUserCollection());
		} catch (Exception e) {
			logger.info("更新用户{}登录时间出错",username);
		}
	    logger.info("=========【输出！！！！！！！！！！】===========credential="+credential+",username="+username);
		return createHandlerResult(credential, new SimplePrincipal(username), null);
	}

	@Override
	public boolean updateUserOnlineState(Integer arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUserLoginTime(Date date, String userName) {
		try {
			Criteria criteria = Criteria.where(getUsernameField()).is(userName).and("userType").ne(-1);
			Update update = new Update();
		    update.set("loginTime", new Date());
		    Query query = Query.query(criteria);
		    getMongoTemplate().updateFirst(query, update, getUserCollection());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
