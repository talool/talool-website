package com.talool.website.pages;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.message.MerchantMessages;
import com.talool.website.util.SecuredPage;

/**
 * 
 * @author dmccuen
 * 
 */
@SecuredPage
public class MessagingPage extends BasePage
{
	private static final long serialVersionUID = -6214364791355264043L;

	public MessagingPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	public String getHeaderTitle()
	{
		return "Messaging";
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new MerchantMessages("messagingPanel"));
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		// Returning null because these will be handled by the tab panels, if at all
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		// Returning null because these will be handled by the tab panels, if at all
		return null;
	}

}
