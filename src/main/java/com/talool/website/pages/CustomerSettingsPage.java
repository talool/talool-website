package com.talool.website.pages;

import org.apache.wicket.markup.html.panel.Panel;

import com.talool.core.MerchantAccount;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

public class CustomerSettingsPage extends BasePage {

	private static final long serialVersionUID = 3482308719491897869L;
	
	public CustomerSettingsPage()
	{
		super();
		
		getActionLink().setVisible(false);
	}

	@Override
	public String getHeaderTitle()
	{
		MerchantAccount ma = SessionUtils.getSession().getMerchantAccount();
		StringBuilder sb = new StringBuilder(ma.getEmail());
		return sb.toString();
	}
	
	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle() {
		return "foo";
	}

}
