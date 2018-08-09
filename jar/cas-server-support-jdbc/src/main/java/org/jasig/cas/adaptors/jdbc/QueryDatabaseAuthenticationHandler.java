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
package org.jasig.cas.adaptors.jdbc;

import java.security.GeneralSecurityException;
import java.util.Date;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;

/**
 * Class that if provided a query that returns a password (parameter of query
 * must be username) will compare that password to a translated version of the
 * password provided by the user. If they match, then authentication succeeds.
 * Default password translator is plaintext translator.
 *
 * @author Scott Battaglia
 * @author Dmitriy Kopylenko
 * @author Marvin S. Addison
 *
 * @since 3.0
 */
public class QueryDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
    @NotNull
    private String sql;
    
    @NotNull
    private String updateOnlineStateSql;
    
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** {@inheritDoc} */
    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
            throws GeneralSecurityException, PreventedException {

        final String username = credential.getUsername();
        //final String encryptedPassword = this.getPasswordEncoder().encode(credential.getPassword());
        final String encryptedPassword = credential.getPassword();
        //logger.info("password is {},md5password is{}",credential.getPassword(),this.getPasswordEncoder().encode(credential.getPassword()));
        try {
            final String dbPassword = getJdbcTemplate().queryForObject(this.sql, String.class, username);
            logger.info("【jdbc】   username is {},dbPassword is {},encryptedPassword is {}",username,dbPassword,encryptedPassword);
            if (!dbPassword.equals(encryptedPassword)) {
                throw new FailedLoginException("Password does not match value on record.");
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
     /*   try {
        	getJdbcTemplate().update(this.updateOnlineStateSql, new Object [] {1,username});
		} catch (Exception e) {
			e.printStackTrace();
			throw new PreventedException("SQL exception while executing update for " + username, e);
		}*/
        return createHandlerResult(credential, new SimplePrincipal(username), null);
    }

    /**
     * @param sql The sql to set.
     */
    public void setSql(final String sql) {
        this.sql = sql;
    }

	public String getUpdateOnlineStateSql() {
		return updateOnlineStateSql;
	}

	public void setUpdateOnlineStateSql(String updateOnlineStateSql) {
		this.updateOnlineStateSql = updateOnlineStateSql;
	}

	@Override
	public boolean updateUserOnlineState(Integer state, String username) {
		logger.info("更新用户 {} 在线状态为{}",username,state);
    	getJdbcTemplate().update(this.updateOnlineStateSql, new Object [] {state,username});
		return false;
	}

	@Override
	public boolean updateUserLoginTime(Date date, String userName) {
		// TODO Auto-generated method stub
		return false;
	}
}
