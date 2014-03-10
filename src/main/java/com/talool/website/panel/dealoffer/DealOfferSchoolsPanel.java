package com.talool.website.panel.dealoffer;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;

public class DealOfferSchoolsPanel extends BaseTabPanel {

	private static final long serialVersionUID = 2170124491668826388L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferSchoolsPanel.class);
	
	private UUID _dealOfferId;

	
	public DealOfferSchoolsPanel(String id, PageParameters parameters) {
		super(id);
		_dealOfferId = UUID.fromString(parameters.get("id").toString());

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final BasePage page = (BasePage) getPage();
		
	}

	@Override
	public String getActionLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
