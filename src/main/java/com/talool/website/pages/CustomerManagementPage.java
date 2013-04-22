package com.talool.website.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.customer.CustomerDealAcquiresPanel;
import com.talool.website.panel.customer.CustomerDealOfferPurchasesPanel;
import com.talool.website.panel.customer.CustomerFriendsPanel;
import com.talool.website.util.SecuredPage;

/**
 * 
 * @author dmccuen
 * 
 */
@SecuredPage
public class CustomerManagementPage extends BaseManagementPage
{
	private static final long serialVersionUID = -6214364791355264043L;

	public CustomerManagementPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	public String getHeaderTitle()
	{
		return "Customers > " + getPageParameters().get("email");
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("Acquired Deals"))
		{

			private static final long serialVersionUID = -6020689518505770059L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new CustomerDealAcquiresPanel(panelId, getPageParameters());
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Purchased Deal Offers"))
		{

			private static final long serialVersionUID = 6987956481004749921L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new CustomerDealOfferPurchasesPanel(panelId, getPageParameters());
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Friends"))
		{

			private static final long serialVersionUID = 8257729247356926661L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new CustomerFriendsPanel(panelId, getPageParameters());
			}
		});

		final AjaxTabbedPanel<ITab> tabbedPanel = new AjaxTabbedPanel<ITab>("tabs", tabs);
		tabbedPanel.setSelectedTab(0);
		add(tabbedPanel);

		// hide the action link for these tabs
		BasePage page = (BasePage) getPage();
		page.getActionLink().setVisible(false);

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
