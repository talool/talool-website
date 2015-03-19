package com.talool.website.panel.dashboard;

import com.talool.core.Customer;
import com.talool.core.DealOfferPurchase;
import com.talool.domain.Properties;
import com.talool.utils.KeyValue;
import com.talool.website.marketing.pages.FundraiserInstructions;
import com.talool.website.models.DealOfferPurchaseListModel;
import com.talool.website.pages.CustomerManagementPage;
import com.talool.website.panel.customer.CustomerDealOfferPurchasesPanel;
import com.talool.website.util.ReceiptParser;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Map;
public class RecentPurchasesPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(CustomerDealOfferPurchasesPanel.class);
	private DealOfferPurchaseListModel model;

	public RecentPurchasesPanel(String id, DealOfferPurchaseListModel model)
	{
		super(id);
		this.model = model;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		

		final ListView<DealOfferPurchase> dops = new ListView<DealOfferPurchase>("rptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DealOfferPurchase> item)
			{
				final DealOfferPurchase dop = item.getModelObject();

				item.setModel(new CompoundPropertyModel<DealOfferPurchase>(dop));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}
				
				String trackingCode = "";
				String price = "";	
				String processingFee = "";
				String taloolFee = "";
				String fundraiserDistribution = "";	
				Properties props = dop.getProperties();
				if (props != null)
				{
					try 
					{
						trackingCode = props.getAsString(KeyValue.merchantCode);
						String receipt = props.getAsString(KeyValue.paymentReceipt);
						if (!StringUtils.isEmpty(receipt))
						{
							Map<String,String> rMap = ReceiptParser.parse(receipt);
							price = rMap.get(ReceiptParser.KEY_COST);
							processingFee = rMap.get(ReceiptParser.KEY_PROCESSING_FEE);
							taloolFee = rMap.get(ReceiptParser.KEY_TALOOL_FEE);
							fundraiserDistribution = rMap.get(ReceiptParser.KEY_FUNDRAISER_DISTRIBUTION);
						}
						
					}
					catch(Exception e)
					{
						LOG.debug("failed to get purchase properties",e);
					}
				}
				
				item.add(new Label("price",price));
				item.add(new Label("fundraiserDistribution",fundraiserDistribution));
				item.add(new Label("fee",taloolFee));
				
				item.add(new Label("dealOffer.title"));
				item.add(new Label("created"));
				item.add(new Label("processorTransactionId"));
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.set("merchant","fundraiser");
				pageParameters.set("cobrand","sales");
				pageParameters.set("code",trackingCode);
				BookmarkablePageLink<String> codeLink = new BookmarkablePageLink<String>("codeLink",FundraiserInstructions.class, pageParameters);
				item.add(codeLink.setVisible(trackingCode!=null));
				codeLink.add(new Label("trackingCode",trackingCode));
				
				Customer customer = dop.getCustomer();
				PageParameters customerParams = new PageParameters();
				customerParams.set("id", customer.getId());
				customerParams.set("email", customer.getEmail());
				String url = (String) urlFor(CustomerManagementPage.class, customerParams);
				ExternalLink emailLink = new ExternalLink("customerLink", Model.of(url),
						new PropertyModel<String>(customer, "email"));
				item.add(emailLink);
				
				item.add(new Label("fullName",customer.getFullName()));

			}

		};

		container.add(dops);
	}
	
}
