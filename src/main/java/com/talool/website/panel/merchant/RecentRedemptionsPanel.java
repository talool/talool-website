package com.talool.website.panel.merchant;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.gmap.api.GLatLng;

import com.talool.core.DealAcquire;
import com.talool.core.MerchantLocation;
import com.talool.website.component.MapPanel;
import com.talool.website.models.DealAcquireListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.CustomerManagementPage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.DistanceUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class RecentRedemptionsPanel extends BaseTabPanel {
	private static final long serialVersionUID = 1L;
	
	private DealAcquireListModel listModel;
	
	public RecentRedemptionsPanel(String id, PageParameters parameters)
	{
		super(id);
		listModel = new DealAcquireListModel();
		listModel.setGetRecent(true);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		BasePage page = (BasePage)getPage();
		final AdminModalWindow modal = page.getModal();
		final SubmitCallBack callback = page.getCallback(modal);
		
		add(new ListView<DealAcquire>("redeemedRptr", listModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<DealAcquire> item)
			{
				final DealAcquire dac = item.getModelObject();
				
				DateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm:ss a z");
				formatter.setTimeZone(getTimeZone());
				String rDate = formatter.format(dac.getRedemptionDate());
				
				item.add(new Label("redemptionDate", rDate));
				
				if (dac.getRedeemedAtGeometry() != null)
				{
					// lookup the closest merchant location
					Geometry geo = dac.getRedeemedAtGeometry();
					MerchantLocation loc = DistanceUtils.getClosestLocation(geo, dac.getDeal().getMerchant());
					Geometry locGeo = loc.getGeometry();
					double distanceInMiles = DistanceUtils.distance(geo, locGeo, "M");
					
					StringBuilder sb;
					if (distanceInMiles < .2)
					{
						// pretty close, so we'll say they redeemed at this location
						sb = new StringBuilder(loc.getAddress1());
						sb.append(", ").append(loc.getCity()).append(", ").append(loc.getStateProvinceCounty()).append(" ").append(loc.getZip());
						PageParameters locParams = new PageParameters();
						locParams.set("id", dac.getDeal().getMerchant().getId());
						String locUrl = (String) urlFor(MerchantManagementPage.class, locParams);
						ExternalLink locLink = new ExternalLink("location", Model.of(locUrl),
								new Model<String>(sb.toString()));
						item.add(locLink);
						item.add(new Label("distance", ""));
					}
					else
					{
						DecimalFormat distanceFormater = new DecimalFormat("###,###.##");
						sb = new StringBuilder();
						sb.append("WARNING: ");
						sb.append(distanceFormater.format(distanceInMiles)).append(" miles from the closest location");
						item.add(new Label("distance", sb.toString()));
						
						item.add(new AjaxLink<Void>("location"){

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								getSession().getFeedbackMessages().clear();
								
								List<GLatLng> markersToShow = new ArrayList<GLatLng>();
								Point pin = (Point) dac.getRedeemedAtGeometry();
								if (pin != null)
								{
									markersToShow.add(new GLatLng(pin.getY(), pin.getX()));
								}
								MapPanel mp = new MapPanel(modal.getContentId(),dac.getDeal().getMerchant(),markersToShow);
								
								modal.setContent(mp);
								modal.setTitle("Redemption Location");
								modal.show(target);
							}
							
						});
					}
					
					
				}
				else
				{
					item.add(new Label("distance", "Unknown"));
					item.add(new Label("location", ""));
				}

				PageParameters dealParams = new PageParameters();
				dealParams.set("id", dac.getDeal().getMerchant().getId());
				String dealUrl = (String) urlFor(MerchantManagementPage.class, dealParams);
				ExternalLink dealLink = new ExternalLink("dealLink", Model.of(dealUrl),
						new PropertyModel<String>(dac.getDeal(), "title"));
				item.add(dealLink);
				
				item.add(new Label("merchantName", dac.getDeal().getMerchant().getName()));
				item.add(new Label("redemptionCode", dac.getRedemptionCode()));
				item.add(new Label("customerName", dac.getCustomer().getFirstName() + " " + dac.getCustomer().getLastName()));
				
				PageParameters customerParams = new PageParameters();
				customerParams.set("id", dac.getCustomer().getId());
				customerParams.set("email", dac.getCustomer().getEmail());
				String url = (String) urlFor(CustomerManagementPage.class, customerParams);
				ExternalLink emailLink = new ExternalLink("emailLink", Model.of(url),
						new PropertyModel<String>(dac.getCustomer(), "email"));
				item.add(emailLink);
				
			}

		});
		
		// hide the action button
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
