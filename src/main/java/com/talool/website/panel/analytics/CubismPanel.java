package com.talool.website.panel.analytics;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class CubismPanel extends Panel {

	private static final long serialVersionUID = -2630791674862025637L;

	private String chartName;
	private List<CubismHorizon> horizons;
	
	public CubismPanel(String id, String chartName, List<CubismHorizon> metrics)
	{
		super(id);
		this.chartName = chartName;
		this.horizons = metrics;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new Label("chartName", chartName));
		
		WebMarkupContainer chart = new WebMarkupContainer("chartCanvas");
		add(chart);
		chart.add(new CubismBehavior(horizons));
		
	}
}
