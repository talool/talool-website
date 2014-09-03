package com.talool.website.util;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.service.ServiceConfig;
import com.talool.utils.GraphiteConstants;
import com.timgroup.statsd.NonBlockingStatsDClient;

public class WebsiteStatsDClient {
	private static NonBlockingStatsDClient client;
	private static WebsiteStatsDClient instance;
	
	private static final Logger LOG = LoggerFactory.getLogger(WebsiteStatsDClient.class);

	private WebsiteStatsDClient() 
	{	
		client = new NonBlockingStatsDClient("talool", "graphite.talool.com", 8125);
	}
	
	public static WebsiteStatsDClient get()
	{
		if (instance == null)
		{
			instance = new WebsiteStatsDClient();
		}
		return instance;
	}
	
	
	/**
	 * Builds the tracking string and sends it to StatsD as a counter
	 * 
	 * @param String action - something like redeem or purchase or gift
	 * @param String subaction - something like facebook or email
	 * @param String object - something like the offer id or the deal id
	 * 
	 * Tracking string format is something like this...
	 * talool.<env>.apps.<app>.<whitelabel>.<platform>.users.<user>.actions.<action>.<subaction>.<object>
	 */
	public void count(Action action, SubAction subaction, UUID object)
	{
		if (action == null) return;

		// Set some default values
		String env = (ServiceConfig.get().isStatsDEnvironmentProduction())?Environment.production.toString():Environment.development.toString();
		String app = Apps.website.toString(); // this is the only one we have, but there could be more in the future
		String whitelabelId = WhiteLabel.core.toString();
		String platform = GraphiteConstants.any;
		String userId = GraphiteConstants.any;
		String subactionString = (subaction==null) ?GraphiteConstants.any:subaction.toString();
		String objString = (object==null) ? GraphiteConstants.any:object.toString();
		
		// Build the key
		StringBuilder sb = new StringBuilder(env);
		sb.append(".apps").append(".").append(app).append(".").append(whitelabelId).append(".").append(platform)
		  .append(".actions.").append(action).append(".").append(subactionString).append(".").append(objString)
		  .append(".users").append(".").append(userId);
		
		LOG.info("count: "+sb.toString());
		client.incrementCounter(sb.toString());
	}
	
	public static final String any = "any";
	
	public enum Action {
		merchant_code_group
	}
	
	public enum SubAction {
		create
	}
	
	public enum DeviceType
	{
		website
	}
	
	public enum Environment
	{
		production, development
	}
	
	public enum Apps
	{
		website
	}
	
	public enum WhiteLabel
	{
		core
	}
}
