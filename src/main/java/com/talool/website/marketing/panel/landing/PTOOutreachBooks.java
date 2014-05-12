package com.talool.website.marketing.panel.landing;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class PTOOutreachBooks extends Panel {

	private static final long serialVersionUID = 1L;
	private int maxPercentage;

	public PTOOutreachBooks(String id, int percentage) {
		super(id);
		maxPercentage = percentage;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new Label("percentage1",maxPercentage));
		add(new Label("percentage2",maxPercentage));
		add(new Label("percentage3",maxPercentage));
	}
	
	

}
