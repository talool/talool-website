package com.talool.website.pages.lists;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;
import org.wicketstuff.gmap.GMap;

import com.talool.core.Merchant;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.MerchantLocationsPanel;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class MerchantLocationsPage extends BasePage
{

	private static final long serialVersionUID = -3762858212653601499L;
	private UUID _merchantId;

	public MerchantLocationsPage()
	{
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new MerchantLocationsPanel("panel", _merchantId));
		
		// preload the map to avoid a race condition with the loading of js
		// dependencies
		GMap map = new GMap("preloadMap");
		add(map);
	}

	@Override
	public String getHeaderTitle()
	{
		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		StringBuilder sb = new StringBuilder(m.getName());
		sb.append(" > Locations");
		return sb.toString();
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "Create New Location";
	}
}
