package com.talool.website.pages;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.analytics.ScoreBoardFactory;
import com.talool.website.util.SecuredPage;

/**
 * 
 * @author dmccuen
 * 
 */
@SecuredPage
public class AnalyticsPage extends BasePage
{
	private static final long serialVersionUID = -6214364791355264043L;

	public AnalyticsPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	public String getHeaderTitle()
	{
		return "Dashboard Analytics";
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(ScoreBoardFactory.createTotalCustomers("totalCustomers").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalRedemptions("totalRedemptions").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalEmailGifts("totalEmailGifts").setRenderBodyOnly(true));

		add(ScoreBoardFactory.createTotalFacebookGifts("totalFacebookGifts").setRenderBodyOnly(true));

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

	public boolean hasActionLink()
	{
		return false;
	}

}
