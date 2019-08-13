package com.cxytiandi.mongodb;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;

import lombok.Data;

@Data
public class MongoPropertiesEx  extends MongoProperties {
	
	private String mongoTemplateName = "mongoTemplate";
	
	private String gridFsTemplateName = "gridFsTemplate";
	
	private String applicationName;

	private boolean showClass = true;
	
    private int minConnectionsPerHost;
    private int maxConnectionsPerHost = 100;
    private int threadsAllowedToBlockForConnectionMultiplier = 5;
    private int serverSelectionTimeout = 1000 * 30;
    private int maxWaitTime = 1000 * 60 * 2;
    private int maxConnectionIdleTime;
    private int maxConnectionLifeTime;
    private int connectTimeout = 1000 * 10;
    private int socketTimeout = 0;
    private boolean socketKeepAlive = false;
    private boolean sslEnabled = false;
    private boolean sslInvalidHostNameAllowed = false;
    private boolean alwaysUseMBeans = false;

    private int heartbeatFrequency = 10000;
    private int minHeartbeatFrequency = 500;
    private int heartbeatConnectTimeout = 20000;
    private int heartbeatSocketTimeout = 20000;
    private int localThreshold = 15;
	
}
