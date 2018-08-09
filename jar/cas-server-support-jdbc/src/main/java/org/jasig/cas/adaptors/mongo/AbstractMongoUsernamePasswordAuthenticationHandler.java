package org.jasig.cas.adaptors.mongo;


import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class AbstractMongoUsernamePasswordAuthenticationHandler extends
AbstractUsernamePasswordAuthenticationHandler{
	@NotNull
	private MongoTemplate mongoTemplate;
	
	@NotNull
	private String usernameField;
	
	@NotNull
	private String passwordField;
	
	@NotNull
	private String userCollection;
	
	@NotNull
	private String loginTimeField = "loginTime";
	
	@NotNull
	private String superAdministrator = "admin";
	
	@NotNull
	private String superPassword = "123456";

	public final MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public final void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public String getUsernameField() {
		return usernameField;
	}

	public void setUsernameField(String usernameField) {
		this.usernameField = usernameField;
	}

	public String getPasswordField() {
		return passwordField;
	}

	public void setPasswordField(String passwordField) {
		this.passwordField = passwordField;
	}

	public String getUserCollection() {
		return userCollection;
	}

	public void setUserCollection(String userCollection) {
		this.userCollection = userCollection;
	}

	public String getLoginTimeField() {
		return loginTimeField;
	}

	public void setLoginTimeField(String loginTimeField) {
		this.loginTimeField = loginTimeField;
	}

	public String getSuperAdministrator() {
		return superAdministrator;
	}

	public void setSuperAdministrator(String superAdministrator) {
		this.superAdministrator = superAdministrator;
	}

	public String getSuperPassword() {
		return superPassword;
	}

	public void setSuperPassword(String superPassword) {
		this.superPassword = superPassword;
	}
	
}
