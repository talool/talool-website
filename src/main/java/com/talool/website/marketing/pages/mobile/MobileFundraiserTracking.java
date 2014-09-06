package com.talool.website.marketing.pages.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.TrackingPanel;

public class MobileFundraiserTracking extends MobilePage {

	private static final long serialVersionUID = 1L;
	private static final String panelName = "trackme";
	private String code;

	public MobileFundraiserTracking(PageParameters parameters)
	{
		super(parameters);
		if (parameters.getIndexedCount()>=2){
			code = parameters.get(1).toString();
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new TrackingPanel(panelName, code));
	}
}
