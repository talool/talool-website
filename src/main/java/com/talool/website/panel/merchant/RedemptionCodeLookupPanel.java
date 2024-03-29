package com.talool.website.panel.merchant;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.objectautocomplete.ObjectAutoCompleteField;
import org.wicketstuff.objectautocomplete.ObjectAutoCompleteSelectionChangeListener;

import com.talool.core.DealAcquire;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.website.component.MerchantAutoCompleteFactory;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.CustomerManagementPage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.DistanceUtils;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SessionUtils;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author clintz
 * 
 */
public class RedemptionCodeLookupPanel extends BaseTabPanel
{
	private static final Logger L = Logger.getLogger(RedemptionCodeLookupPanel.class);
	private static final long serialVersionUID = 3634980968241854373L;

	private UUID merchantId;
	private UUID redemptionMerchantId;
	private String redemptionCode;

	public RedemptionCodeLookupPanel(String id, PageParameters parameters)
	{
		super(id);
		merchantId = UUID.fromString(parameters.get("id").toString());
		redemptionMerchantId = merchantId;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		Form<Void> form = new Form<Void>("searchForm")
		{

			@Override
			protected void onSubmit()
			{
				super.onSubmit();

				try
				{
					List<DealAcquire> dacs = taloolService.getRedeemedDealAcquires(redemptionMerchantId, redemptionCode.toUpperCase());
					RedemptionCodeLookupPanel.this.get("redeemedRptr").setDefaultModel(Model.of(dacs));
					RedemptionCodeLookupPanel.this.get("redeemedRptr").setVisible(true);
				}
				catch (ServiceException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			private static final long serialVersionUID = 773304007969659116L;

		};

		final TextField<String> codeField = new TextField<String>("redemptionCode", new PropertyModel<String>(this, "redemptionCode"));
		form.add(codeField.setRequired(true).setOutputMarkupId(true));
		add(form);

		add(new ListView<DealAcquire>("redeemedRptr")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<DealAcquire> item)
			{
				DealAcquire dac = item.getModelObject();
				
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
						item.add(new Label("location", ""));
					}
					
					
				}
				else
				{
					item.add(new Label("distance", ""));
					item.add(new Label("location", "Unknown"));
				}

				PageParameters dealParams = new PageParameters();
				dealParams.set("id", dac.getDeal().getMerchant().getId());
				String dealUrl = (String) urlFor(MerchantManagementPage.class, dealParams);
				ExternalLink dealLink = new ExternalLink("dealLink", Model.of(dealUrl),
						new PropertyModel<String>(dac.getDeal(), "title"));
				item.add(dealLink);
				
				item.add(new Label("offerName", dac.getDeal().getDealOffer().getTitle()));
				item.add(new Label("customerName", dac.getCustomer().getFirstName() + " " + dac.getCustomer().getLastName()));
				
				PageParameters customerParams = new PageParameters();
				customerParams.set("id", dac.getCustomer().getId());
				customerParams.set("email", dac.getCustomer().getEmail());
				String url = (String) urlFor(CustomerManagementPage.class, customerParams);
				ExternalLink emailLink = new ExternalLink("emailLink", Model.of(url),
						new PropertyModel<String>(dac.getCustomer(), "email"));
				item.add(emailLink);
				
			}

		}.setVisible(false));
		
		final MerchantAccount account = SessionUtils.getSession().getMerchantAccount();
		
		// Auto-complete search field for merchant selection
		final WebMarkupContainer acContainer = new WebMarkupContainer("acContainer");
		form.add(acContainer);
		// only visible for certain users
		acContainer.setVisible(PermissionUtils.isSuperUser(account) || PermissionUtils.isPublisher(account.getMerchant()));
		MerchantAutoCompleteFactory acFactory = new MerchantAutoCompleteFactory(SessionUtils.getSession().getMerchantAccount().getMerchant());
		ObjectAutoCompleteField<Merchant, UUID> acField = acFactory.builder().build("ac", new PropertyModel<UUID>(this, "redemptionMerchantId"));
	    acField.setRequired(true);
	    acContainer.add(acField);
	    acField.registerForUpdateOnSelectionChange(new ObjectAutoCompleteSelectionChangeListener<UUID>(){
			private static final long serialVersionUID = -2873351122556896745L;

			@Override
			public void selectionChanged(AjaxRequestTarget target, IModel<UUID> model) {
				// set the focus on redemption code field
				target.focusComponent(codeField);
			}
	    	
	    });
		
	 
	    // hide the action button
	    final BasePage page = (BasePage) getPage();
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