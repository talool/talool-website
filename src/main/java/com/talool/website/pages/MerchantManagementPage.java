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
import org.wicketstuff.gmap.GMap;

import com.talool.core.Merchant;
import com.talool.utils.KeyValue;
import com.talool.website.models.MerchantModel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.MerchantAccountsPanel;
import com.talool.website.panel.merchant.MerchantAnalyticsPanel;
import com.talool.website.panel.merchant.MerchantDealOffersPanel;
import com.talool.website.panel.merchant.MerchantDealsPanel;
import com.talool.website.panel.merchant.MerchantLocationsPanel;
import com.talool.website.panel.merchant.MerchantSummaryPanel;
import com.talool.website.panel.merchant.PaymentProcessingPanel;
import com.talool.website.panel.merchant.PublisherAnalyticsPanel;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
@SecuredPage
public class MerchantManagementPage extends BaseManagementPage
{
	private static final long serialVersionUID = -6214364791355264043L;
	private UUID _merchantId;

	public MerchantManagementPage(PageParameters parameters)
	{
		super(parameters);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	public String getHeaderTitle()
	{
		Merchant merchant = new MerchantModel(_merchantId, true).getObject();
		if (merchant.getProperties().getAsBool(KeyValue.publisher))
		{
			return "Publishers > " + getPageParameters().get("name");
		}
		else
		{
			return "Merchants > " + getPageParameters().get("name");
		}

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		List<ITab> tabs = new ArrayList<ITab>();

		tabs.add(new AbstractTab(new Model<String>("Summary"))
		{

			private static final long serialVersionUID = 5853871222415506440L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MerchantSummaryPanel(panelId, getPageParameters());
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Locations"))
		{

			private static final long serialVersionUID = 5458420599727527535L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new MerchantLocationsPanel(panelId, getPageParameters());
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

		if (PermissionUtils.isPublisher(_merchantId))
		{
			tabs.add(new AbstractTab(Model.of("Payment Processing"))
			{

				private static final long serialVersionUID = 5853871222415506440L;

				@Override
				public Panel getPanel(String panelId)
				{
					return new PaymentProcessingPanel(panelId, getPageParameters());
				}

			});
		}

		if (PermissionUtils.isPublisher(_merchantId))
		{
			tabs.add(new AbstractTab(new Model<String>("Books"))
			{

				private static final long serialVersionUID = 5853871222415506440L;

				@Override
				public Panel getPanel(String panelId)
				{
					return new MerchantDealOffersPanel(panelId, getPageParameters());
				}
			});
//			tabs.add(new AbstractTab(new Model<String>("Recent Purchases"))
//			{
//
//				private static final long serialVersionUID = 5853871222415506440L;
//
//				@Override
//				public Panel getPanel(String panelId)
//				{
//					DealOfferPurchaseListModel dopModel = new DealOfferPurchaseListModel();
//					dopModel.setMerchantId(_merchantId);
//					return new RecentPurchasesPanel(panelId, dopModel);
//				}
//			});
		}
		else
		{
			tabs.add(new AbstractTab(new Model<String>("Deals"))
			{
				private static final long serialVersionUID = 6405610365875810783L;

				@Override
				public Panel getPanel(String panelId)
				{
					return new MerchantDealsPanel(panelId, getPageParameters());
				}
			});
		}

		if (PermissionUtils.canViewAnalytics(SessionUtils.getSession().getMerchantAccount()))
		{
			tabs.add(new AbstractTab(new Model<String>("Analytics"))
			{

				private static final long serialVersionUID = 5853871222415506440L;

				@Override
				public Panel getPanel(String panelId)
				{
					Merchant merchant = new MerchantModel(_merchantId, true).getObject();
					if (merchant.getProperties().getAsBool(KeyValue.publisher))
					{
						return new PublisherAnalyticsPanel(panelId, getPageParameters());
					}
					else
					{
						return new MerchantAnalyticsPanel(panelId, getPageParameters());
					}
				}

			});
		}

		final AjaxTabbedPanel<ITab> tabbedPanel = new AjaxTabbedPanel<ITab>("tabs", tabs)
		{

			private static final long serialVersionUID = -9186300115065742114L;

			@Override
			protected void onAjaxUpdate(AjaxRequestTarget target)
			{
				super.onAjaxUpdate(target);
				getSession().getFeedbackMessages().clear();

				MerchantManagementPage page = (MerchantManagementPage) this.getPage();
				target.add(page.getFeedback());
				target.add(page.getActionLink());
			}

		};
		tabbedPanel.setSelectedTab(0);
		add(tabbedPanel);

		// preload the map to avoid a race condition with the loading of js
		// dependencies
		GMap map = new GMap("preloadMap");
		add(map);

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
