package com.talool.website.panel.merchant;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wicketstuff.objectautocomplete.ObjectAutoCompleteField;

import com.talool.core.DealAcquire;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.website.component.MerchantAutoCompleteBuilder;
import com.talool.website.pages.CustomerManagementPage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

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

		form.add(new TextField<String>("redemptionCode", new PropertyModel<String>(this, "redemptionCode")).setRequired(true));
		add(form);

		add(new ListView<DealAcquire>("redeemedRptr")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<DealAcquire> item)
			{
				DealAcquire dac = item.getModelObject();
				
				Date redemptionDate = dac.getRedemptionDate();
				
				//SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm:ss a z");
				//sdf.setTimeZone(TimeZone.getDefault());
				//String rDate = sdf.format(now);
				
				DateTime localDate = new DateTime(redemptionDate.getTime());
				DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, MMM d, yyyy 'at' h:mm:ss a z");
				String rDate = formatter.print(localDate);
				
				item.add(new Label("redemptionDate", rDate));
				
				if (dac.getRedeemedAtGeometry() != null)
				{
					// TODO lookup the closest merchant location
					item.add(new Label("redeemedLocation", "longitude: " + dac.getRedeemedAtGeometry().getCoordinate().x + " latitude:"
							+ dac.getRedeemedAtGeometry().getCoordinate().x));
				}
				else
				{
					item.add(new Label("redeemedLocation", "Unknown"));
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
		
		// Auto-complete search field for merchant selection
		MerchantAutoCompleteBuilder acBuilder = new MerchantAutoCompleteBuilder(SessionUtils.getSession().getMerchantAccount().getMerchant());
	    ObjectAutoCompleteField<Merchant, UUID> acField = acBuilder.build("ac", new PropertyModel<UUID>(this, "redemptionMerchantId"));
	    acField.setRequired(true);
	    form.add(acField);
	    
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


}