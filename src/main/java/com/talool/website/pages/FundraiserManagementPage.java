package com.talool.website.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.FundraiserSummaryPanel;
import com.talool.website.panel.merchant.MerchantAccountsPanel;
import com.talool.website.util.SecuredPage;

/**
 * 
 * @author dmccuen
 * 
 */
@SecuredPage
public class FundraiserManagementPage extends BaseManagementPage
{
	private static final long serialVersionUID = -6214364791355264043L;
	private UUID _merchantId;
	
	public FundraiserManagementPage(PageParameters parameters)
	{
		super(parameters);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	public String getHeaderTitle()
	{
		return "Fundraisers > " + getPageParameters().get("name");
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		//final Merchant merchant = new MerchantModel(_merchantId, true).getObject();

		List<ITab> tabs = new ArrayList<ITab>();
		

		tabs.add(new AbstractTab(new Model<String>("Summary"))
		{
			private static final long serialVersionUID = 6405610365875810783L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new FundraiserSummaryPanel(panelId, getPageParameters());
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Accounts"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MerchantAccountsPanel(panelId, getPageParameters());
			}
		});


		final AjaxTabbedPanel<ITab> tabbedPanel = new AjaxTabbedPanel<ITab>("tabs", tabs)
		{

			private static final long serialVersionUID = -9186300115065742114L;

			@Override
			protected void onAjaxUpdate(AjaxRequestTarget target)
			{
				super.onAjaxUpdate(target);
				getSession().getFeedbackMessages().clear();;
				BasePage page = (BasePage) this.getPage();
				target.add(page.getFeedback());
				target.add(page.getActionLink());
			}

		};
		tabbedPanel.setSelectedTab(0);
		add(tabbedPanel);

	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		// Returning null because these will be handled by the tab panels
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		// Returning null because these will be handled by the tab panels
		return null;
	}
}
