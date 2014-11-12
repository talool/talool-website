package com.talool.website.panel.merchant;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.FundraiserTrackingRollup;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;

public class FundraiserTrackingPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 522375793977325630L;

	private PageParameters parameters;

	public FundraiserTrackingPanel(String id, PageParameters parameters)
	{
		super(id);
		this.parameters = parameters;
		this.parameters.set(0, "payback");
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		String url = (String) urlFor(FundraiserTrackingRollup.class, parameters);
		add(new ExternalLink("externalLink", Model.of(url)));
		
		add(new FundraiserTrackingRollupPanel("container", parameters, true));

		// hide the action button
		final BasePage page = (BasePage) this.getPage();
		page.getActionLink().add(new AttributeModifier("class", "hide"));
	}

	@Override
	public String getActionLabel()
	{
		return null;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

}
