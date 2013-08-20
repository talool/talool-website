package com.talool.website.pages.corporate;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.corporate.TermsPanel;


public class TermsOfService extends BaseCorporatePage {

	private static final long serialVersionUID = 1L;
	
	public TermsOfService(PageParameters params) {
		super(params);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new TermsPanel("terms"));
	}

}
