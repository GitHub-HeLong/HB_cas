package org.jasig.cas.adaptors.base;

import org.jasig.cas.log.LoginLogHandler;

public abstract class AbstractLoginLogHandler implements LoginLogHandler{
	private String name;

    public String getName() {
        return this.name != null ? this.name : getClass().getSimpleName();
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
