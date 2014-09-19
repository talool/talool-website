package com.talool.website.panel.customer;

import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DealOfferPurchase;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.service.ServiceFactory;
import com.talool.utils.KeyValue;
import com.talool.website.marketing.pages.FundraiserInstructions;
import com.talool.website.models.DealOfferPurchaseListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.PropertiesPanel;
import com.talool.website.util.CobrandUtil;
import com.talool.website.util.ReceiptParser;

public class CustomerDealOfferPurchasesPanel extends Panel
{
	private static final long serialVersionUID = -1572792713158372783L;
	private static final Logger LOG = Logger.getLogger(CustomerDealOfferPurchasesPanel.class);
	private UUID _customerId;

	public CustomerDealOfferPurchasesPanel(String id, PageParameters parameters)
	{
		super(id);
		_customerId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final BasePage page = (BasePage) getPage();
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		final AdminModalWindow modalProps = new AdminModalWindow("modalProps");
		modalProps.setInitialWidth(650);
		container.add(modalProps.setOutputMarkupId(true));

		DealOfferPurchaseListModel model = new DealOfferPurchaseListModel();
		model.setCustomerId(_customerId);
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
						Map<String,String> rMap = ReceiptParser.parse(receipt);
						price = rMap.get(ReceiptParser.KEY_COST);
						processingFee = rMap.get(ReceiptParser.KEY_PROCESSING_FEE);
						taloolFee = rMap.get(ReceiptParser.KEY_TALOOL_FEE);
						fundraiserDistribution = rMap.get(ReceiptParser.KEY_FUNDRAISER_DISTRIBUTION);
						
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
				
				PageParameters pageParameters = CobrandUtil.getCobrandedPageParameters(trackingCode);
				pageParameters.set("code",trackingCode);
				BookmarkablePageLink<String> codeLink = new BookmarkablePageLink<String>("codeLink",FundraiserInstructions.class, pageParameters);
				item.add(codeLink.setVisible(trackingCode!=null));
				codeLink.add(new Label("trackingCode",trackingCode));
				
				item.add(new AjaxLink<Void>("editProps")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();

						PropertiesPanel panel = new PropertiesPanel(modalProps.getContentId(), Model.of(dop.getProperties()),
								DealOfferPurchase.class)
						{

							private static final long serialVersionUID = -6061721033345142501L;

							@Override
							public void saveEntityProperties(Properties props, AjaxRequestTarget target)
							{
								try
								{
									ServiceFactory.get().getTaloolService().merge(dop);
								}
								catch (ServiceException e)
								{
									LOG.error("Failed to merge purchase after editing properties", e);
								}
								target.appendJavaScript("window.parent.Wicket.Window.current.autoSizeWindow();");
								target.add(container);
							}
							
						};

						modalProps.setContent(panel);
						modalProps.setTitle("Manage Purchase Properties");
						modalProps.show(target);
					}

				}.setVisible(page.isSuperUser));
			}

		};

		container.add(dops);
	}
	

}