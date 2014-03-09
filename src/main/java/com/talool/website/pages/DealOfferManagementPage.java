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

import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.website.models.DealOfferModel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.dealoffer.DealOfferAnalyticsPanel;
import com.talool.website.panel.dealoffer.DealOfferDealsPanel;
import com.talool.website.panel.dealoffer.DealOfferSummaryPanel;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen
 * 
 */
@SecuredPage
public class DealOfferManagementPage extends BaseManagementPage
{
	private static final long serialVersionUID = -6214364791355264043L;
	private UUID _dealOfferId;
	
	public DealOfferManagementPage(PageParameters parameters)
	{
		super(parameters);
		_dealOfferId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	public String getHeaderTitle()
	{
		return "Books > " + getPageParameters().get("name");
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final DealOffer offer = new DealOfferModel(_dealOfferId).getObject();

		List<ITab> tabs = new ArrayList<ITab>();
		
		if (!isKirkeBook(offer))
		{
			tabs.add(new AbstractTab(new Model<String>("Summary"))
			{
				private static final long serialVersionUID = 6405610365875810783L;
	
				@Override
				public Panel getPanel(String panelId)
				{
					return new DealOfferSummaryPanel(panelId, getPageParameters());
				}
			});
		}
		
		tabs.add(new AbstractTab(new Model<String>("Deals"))
		{
			private static final long serialVersionUID = 6405610365875810783L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new DealOfferDealsPanel(panelId, getPageParameters());
			}
		});
		
		// TODO should not be dependent on isActive, but rather if there have been purchases or activations
		if (!isKirkeBook(offer) && 
				offer.isActive() && 
				PermissionUtils.canViewAnalytics(SessionUtils.getSession().getMerchantAccount().getMerchant()))
		{
			tabs.add(new AbstractTab(new Model<String>("Analytics"))
			{
				private static final long serialVersionUID = 6405610365875810783L;
	
				@Override
				public Panel getPanel(String panelId)
				{
					return new DealOfferAnalyticsPanel(panelId, getPageParameters());
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
				getSession().getFeedbackMessages().clear();;
				BasePage page = (BasePage) this.getPage();
				target.add(page.getFeedback());
			}

		};
		tabbedPanel.setSelectedTab(0);
		add(tabbedPanel);

	}
	
	private boolean isKirkeBook(DealOffer offer) {
		return offer.getType().equals(DealType.KIRKE_BOOK);
	}

	@Override
	public boolean hasActionLink() {
		return false;
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
