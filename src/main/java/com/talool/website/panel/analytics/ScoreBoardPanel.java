package com.talool.website.panel.analytics;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * 
 * @author clintz
 * 
 */
public class ScoreBoardPanel extends Panel
{
	private static final long serialVersionUID = -2193478480235823659L;

	private String scoreBoardLabel;
	private IModel<MetricCount> model;
	
	public ScoreBoardPanel(String id, String scoreBoardLabel, IModel<MetricCount> model)
	{
		super(id);
		this.scoreBoardLabel = scoreBoardLabel;
		this.model = model;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new Label("scoreBoardLabel", scoreBoardLabel));

		add(new Label("scoreBoardMetric", model.getObject().getCount()));
		
		add(new Label("total", model.getObject().getTotal()));
		
		add(new Label("percentage", model.getObject().getPercentage()));

	}

}
