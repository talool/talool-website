package com.talool.website.panel.customer;

import java.util.Date;
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
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DealOfferPurchase;
import com.talool.core.RefundResult;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.service.ServiceFactory;
import com.talool.utils.KeyValue;
import com.talool.utils.SafeSimpleDateFormat;
import com.talool.website.Constants;
import com.talool.website.component.ConfirmationAjaxLink;
import com.talool.website.marketing.pages.FundraiserInstructions;
import com.talool.website.models.DealOfferPurchaseListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.PropertiesPanel;
import com.talool.website.util.CobrandUtil;
import com.talool.website.util.ReceiptParser;
import com.talool.website.util.SessionUtils;

public class CustomerDealOfferPurchasesPanel extends Panel
{
	private static final long serialVersionUID = -1572792713158372783L;
	private static final Logger LOG = Logger.getLogger(CustomerDealOfferPurchasesPanel.class);
	private static final SafeSimpleDateFormat DATE_FORMAT = new SafeSimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);

	private UUID _customerId;

	public CustomerDealOfferPurchasesPanel(String id, PageParameters parameters)
	{
		super(id);
		_customerId = UUID.fromString(parameters.get("id").toString());
	}

	private class RefundModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 8360850206516226216L;
		private DealOfferPurchase dop;

		public RefundModel(final DealOfferPurchase dop)
		{
			this.dop = dop;
		}

		@Override
		public String getObject()
		{
			if (!dop.isRefundedOrVoided())
			{
				return null;
			}

			final StringBuilder sb = new StringBuilder();

			Long refundTime = dop.getProperties().getAsLong(KeyValue.processorRefundDate);

			if (refundTime != null)
			{
				sb.append("Refund ").append("<br/>").append(DATE_FORMAT.format(new Date(refundTime)));
			}
			else
			{
				refundTime = dop.getProperties().getAsLong(KeyValue.processorVoidDate);
				if (refundTime != null)
				{
					sb.append("Void ").append("<br/>").append(DATE_FORMAT.format(new Date(refundTime)));
				}
			}

			return sb.toString();
		}

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

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
						Map<String, String> rMap = ReceiptParser.parse(receipt);
						price = rMap.get(ReceiptParser.KEY_COST);
						processingFee = rMap.get(ReceiptParser.KEY_PROCESSING_FEE);
						taloolFee = rMap.get(ReceiptParser.KEY_TALOOL_FEE);
						fundraiserDistribution = rMap.get(ReceiptParser.KEY_FUNDRAISER_DISTRIBUTION);

					}
					catch (Exception e)
					{
						LOG.debug("failed to get purchase properties", e);
					}
				}

				item.add(new Label("price", price));
				item.add(new Label("fundraiserDistribution", fundraiserDistribution));
				item.add(new Label("fee", taloolFee));

				item.add(new Label("dealOffer.title"));
				item.add(new Label("created", Model.of(DATE_FORMAT.format(dop.getCreated()))));
				item.add(new Label("processorTransactionId"));

				item.add(new Label("refundDate", new RefundModel(dop)).setEscapeModelStrings(false));

				PageParameters pageParameters = CobrandUtil.getCobrandedPageParameters(trackingCode);
				pageParameters.set("code", trackingCode);
				BookmarkablePageLink<String> codeLink = new BookmarkablePageLink<String>("codeLink", FundraiserInstructions.class,
						pageParameters);
				item.add(codeLink.setVisible(trackingCode != null));
				codeLink.add(new Label("trackingCode", trackingCode));

				item.add(new ConfirmationAjaxLink<Void>("refund", "Are you sure you want to refund/void " + dop.getDealOffer().getTitle()
						+ " ?")
				{

					private static final long serialVersionUID = -4592149231430681542L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						target.add(((BasePage) getPage()).getFeedback());
						target.add(container);
						getSession().getFeedbackMessages().clear();
						try
						{
							final RefundResult refundResult = ServiceFactory.get().getTaloolService().refundOrVoid(dop, false);

							SessionUtils.infoMessage("Succesful ", refundResult.getRefundType().toString(), " of ", dop.getDealOffer().getTitle(),
									".  Removed ", String.valueOf(refundResult.getTotalDealAcquiresRemoved()), " deal acquires.");
						}
						catch (ServiceException e)
						{
							LOG.error(e.getMessage(), e);
							SessionUtils.errorMessage(e.getMessage());
						}
						catch (Exception e)
						{
							LOG.error(e.getMessage(), e);
							SessionUtils.errorMessage(e.getMessage());
						}

					}
				});
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

				}.setVisible(((BasePage) getPage()).isSuperUser));
			}

		};

		container.add(dops);
	}
}