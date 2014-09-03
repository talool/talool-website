package com.talool.website.pages.lists;

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

import com.talool.website.pages.BaseManagementPage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.dashboard.MerchantRecentRedemptionsPanel;
import com.talool.website.panel.merchant.RecentRedemptionsPanel;
import com.talool.website.panel.merchant.RedemptionCodeLookupPanel;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class MerchantRedemptionCodePage extends BaseManagementPage
{

	private static final long serialVersionUID = -3832602225330266637L;
	private UUID _merchantId;

	public MerchantRedemptionCodePage(PageParameters parameters)
	{
		super(parameters);
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
	}

	@Override
	public String getHeaderTitle()
	{
		return "Redemptions";
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		
		
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("Recent Redemptions"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				if (isSuperUser)
				{
					return new RecentRedemptionsPanel(panelId, getPageParameters());
				}
				else
				{
					return new MerchantRecentRedemptionsPanel(panelId, _merchantId);
				}
			}
			
			
		});
		
		tabs.add(new AbstractTab(new Model<String>("Search"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				PageParameters params = getPageParameters();
				params.add("id", _merchantId);
				return new RedemptionCodeLookupPanel(panelId, params);
			}
		});

		final AjaxTabbedPanel<ITab> tabbedPanel = new AjaxTabbedPanel<ITab>("tabs", tabs)
		{

			private static final long serialVersionUID = -9186300115065742114L;

			@Override
			protected void onAjaxUpdate(AjaxRequestTarget target)
			{
				super.onAjaxUpdate(target);
				getSession().getFeedbackMessages().clear();
				
				MerchantRedemptionCodePage page = (MerchantRedemptionCodePage) this.getPage();
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
