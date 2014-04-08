package com.talool.website.panel.analytics;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.talool.service.ServiceConfig;
import com.talool.utils.GraphiteConstants.Action;
import com.talool.utils.GraphiteConstants.Apps;
import com.talool.utils.GraphiteConstants.DeviceType;
import com.talool.utils.GraphiteConstants.Environment;
import com.talool.utils.GraphiteConstants.SubAction;

public class GraphiteMetric implements Serializable
{

	private static final long serialVersionUID = 1L;
	private final String title;
	private final String definition;
	private static final String wildcard = "*";
	
	
	public GraphiteMetric(String title)
	{
		this.title = title;
		StringBuilder sb = new StringBuilder();
		sb.append("sumSeries(stats.talool.")
		  .append(getEnvironment())
		  .append(".apps.mobile.core.*.actions.*.*.any.users.any)");
		this.definition = sb.toString();
	}
	
	public GraphiteMetric(String title, DeviceType device, Action action, SubAction subaction, List<UUID> ids)
	{
		this.title = title;
		String appString = Apps.mobile.toString();
		String deviceString = (device==null) ? wildcard:device.toString();
		String actionString =  (action==null) ? wildcard:action.toString();
		
		String subactionString =  (subaction==null) ? wildcard:subaction.toString();
		if (subaction != null && subaction.equals(SubAction.credit_wildcard)) subactionString = "credit*";
		
		String objectString =  (ids==null) ? wildcard:getObjectString(ids);
		
		StringBuilder sb = new StringBuilder("sumSeries(stats.talool.");
		sb.append(getEnvironment())
		  .append(".apps.")
		  .append(appString).append(".*.").append(deviceString)
		  .append(".actions.")
		  .append(actionString).append(".").append(subactionString).append(".").append(objectString)
		  .append(".users.*)");
		
		this.definition = sb.toString();
	}

	public String getTitle() 
	{
		return title;
	}

	public String getDefinition() 
	{
		return definition;
	}
	
	public String getObjectString(List<UUID> ids)
	{
		StringBuilder sb = new StringBuilder();
		if (ids.isEmpty())
		{
			sb.append(wildcard);
		}
		else if (ids.size() == 1)
		{
			sb.append(ids.get(0));
		}
		else
		{
			// TODO be mindful of the URL length
			sb.append("{");
			boolean addComma = false;
			for (UUID id : ids)
			{
				if (addComma)
				{
					sb.append(",");
				}
				addComma = true;
				sb.append(id);
			}
			sb.append("}");
		}
		return sb.toString();
	}
	
	private String getEnvironment()
	{
		return (ServiceConfig.get().isStatsDEnvironmentProduction())?Environment.production.toString():Environment.development.toString();
	}
	

}
