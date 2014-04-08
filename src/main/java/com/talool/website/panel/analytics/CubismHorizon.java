package com.talool.website.panel.analytics;

import java.io.Serializable;
import java.util.List;

public class CubismHorizon implements Serializable
{

	private static final long serialVersionUID = 1L;
	private final String title;
	private final List<GraphiteMetric> metrics;
	
	
	public CubismHorizon(String title, List<GraphiteMetric> metrics)
	{
		this.title = title;
		this.metrics = metrics;
	}

	public String getTitle() 
	{
		return title;
	}

	public List<GraphiteMetric> getMetrics() 
	{
		return metrics;
	}
	

}
