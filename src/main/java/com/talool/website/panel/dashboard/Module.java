package com.talool.website.panel.dashboard;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;

public class Module extends Panel {
	

	private static final long serialVersionUID = 1L;
	protected UUID merchantId;

	public Module(String id, UUID merchantId)
	{
		super(id);
		this.merchantId = merchantId;
	}
}
