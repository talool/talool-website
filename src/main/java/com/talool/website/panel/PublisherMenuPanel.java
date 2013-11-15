package com.talool.website.panel;

import java.util.UUID;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.pages.lists.MerchantsPage;
import com.talool.website.util.SessionUtils;

public class PublisherMenuPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PublisherMenuPanel.class);
	
	public PublisherMenuPanel(final String id)
	{
		super(id);
		
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		UUID merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
		PageParameters parameters = new PageParameters();
        parameters.add("id",merchantId);
        
		BookmarkablePageLink merchant = new BookmarkablePageLink("merchants", MerchantsPage.class, parameters);
		if (getPage().getClass().equals(MerchantsPage.class)) {
			// mark the merchants link as selected
			
		}
	}
}
