package org.jasig.cas.permission.activemq.service;

import java.io.Serializable;
import java.util.Map;

import javax.jms.Destination;

public interface IProducerService {
	public void sendMessage(Destination destination, 
			final Serializable message);
	public void sendMessage(Destination destination,
			final String message);
	public void sendMessage(Destination destination,
			final Map<String, Object> message);
}
