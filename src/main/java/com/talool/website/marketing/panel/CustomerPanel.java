package com.talool.website.marketing.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class CustomerPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private String merchantName;

	public CustomerPanel(String id, String merchantName) {
		super(id);
		this.merchantName = merchantName;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Label("n1",merchantName));
		add(new Label("n2",merchantName));
		add(new Label("n3",merchantName));
		add(new Label("n4",merchantName));
	}

}
