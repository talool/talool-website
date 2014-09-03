package com.talool.website.panel.dashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.service.AnalyticService.RecentRedemption;
import com.talool.website.models.RecentRedemptionListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.CustomerManagementPage;

public class MerchantRecentRedemptionsPanel extends Panel {

	private static final long serialVersionUID = 1L;
	protected UUID merchantId;

	public MerchantRecentRedemptionsPanel(String id, UUID merchantId)
	{
		super(id);
		this.merchantId = merchantId;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		RecentRedemptionListModel model = new RecentRedemptionListModel();
		model.setMerchantId(merchantId);
		
		if (model.getObject().size() > 10)
		{
			info("Showing the 10 most recent redemptions for your business.");
		}
		
		final ListView<RecentRedemption> redemptions = new ListView<RecentRedemption>("rptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RecentRedemption> item)
			{
				RecentRedemption redemption = item.getModelObject();

				item.setModel(new CompoundPropertyModel<RecentRedemption>(redemption));
				
				item.add(new Label("title"));
				item.add(new Label("code"));
				
				DateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm:ss a z");
				formatter.setTimeZone(getTimeZone());
				String rDate = formatter.format(redemption.date);
				
				item.add(new Label("date", rDate));
				
				PageParameters customerParams = new PageParameters();
				customerParams.set("id", redemption.customerId);
				customerParams.set("email", redemption.customerName); // TODO fix hack
				String url = (String) urlFor(CustomerManagementPage.class, customerParams);
				ExternalLink emailLink = new ExternalLink("emailLink", Model.of(url),
						Model.of(redemption.customerName));
				item.add(emailLink);

			}

		};
		add(redemptions);
		
		// hide the action button
	    final BasePage page = (BasePage) getPage();
	    page.getActionLink().add(new AttributeModifier("class", "hide"));
	}
	
	public TimeZone getTimeZone() 
	{
		// start with the mountain timezone
		TimeZone timezone = TimeZone.getTimeZone("MST");
		
		// update with the client's timezone if possible
		WebClientInfo info = (WebClientInfo)getSession().getClientInfo();
		if (info != null)
		{
			TimeZone tz = info.getProperties().getTimeZone();
			if (tz != null)
			{
				timezone = tz;
			}
		}	
		
		return timezone;
	}
	
}
