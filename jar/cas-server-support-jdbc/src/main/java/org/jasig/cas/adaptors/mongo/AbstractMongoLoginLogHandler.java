package org.jasig.cas.adaptors.mongo;

import javax.validation.constraints.NotNull;

import org.jasig.cas.adaptors.base.AbstractLoginLogHandler;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class AbstractMongoLoginLogHandler extends AbstractLoginLogHandler {
	@NotNull
	private MongoTemplate mongoTemplate;
	
	@NotNull
	private String logCollection;
	
	public final MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public final void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public final String getLogCollection() {
		return logCollection;
	}

	public final void setLogCollection(String logCollection) {
		this.logCollection = logCollection;
	}
	
	
}
