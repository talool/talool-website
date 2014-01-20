package com.talool.website.pages.lists;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.website.behaviors.AJAXDownload;
import com.talool.website.component.ConfirmationIndicatingAjaxLink;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantDealOfferPanel;
import com.talool.website.service.PermissionService;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class DealOffersPage extends BasePage
{
	private static final Logger LOG = Logger.getLogger(DealOffersPage.class);
	private static final long serialVersionUID = 2102415289760762365L;
	private int downloadCodeCount;

	public DealOffersPage()
	{
		super();
	}

	public DealOffersPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		boolean canViewAllBooks = PermissionService.get().canViewAnalytics(
				SessionUtils.getSession().getMerchantAccount().getEmail());

		DealOfferListModel model = new DealOfferListModel();
		final StringBuilder sb = new StringBuilder();

		if (!canViewAllBooks)
		{
			model.setMerchantId(SessionUtils.getSession().getMerchantAccount().getMerchant().getId());
		}

		final ListView<DealOffer> customers = new ListView<DealOffer>("offerRptr",
				model)
		{

			private static final long serialVersionUID = 4104816505968727445L;

			@Override
			protected void populateItem(ListItem<DealOffer> item)
			{
				sb.setLength(0);
				final DealOffer dealOffer = item.getModelObject();
				final UUID dealOfferId = dealOffer.getId();
				final String title = dealOffer.getTitle();

				item.setModel(new CompoundPropertyModel<DealOffer>(dealOffer));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				PageParameters dealsParams = new PageParameters();
				dealsParams.set("id", dealOffer.getId());
				dealsParams.set("name", dealOffer.getTitle());
				String url = (String) urlFor(DealOfferDealsPage.class, dealsParams);
				ExternalLink titleLink = new ExternalLink("titleLink", Model.of(url),
						new PropertyModel<String>(dealOffer, "title"));
				item.add(titleLink);

				item.add(new Label("merchant.name"));
				item.add(new Label("price"));
				item.add(new Label("dealType"));
				item.add(new Label("expires"));
				item.add(new Label("isActive"));

				BasePage page = (BasePage) this.getPage();
				final AdminModalWindow modal = page.getModal();
				final SubmitCallBack callback = page.getCallback(modal);
				item.add(new AjaxLink<Void>("editLink")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantDealOfferPanel panel = new MerchantDealOfferPanel(modal.getContentId(),
								callback, dealOfferId);
						modal.setContent(panel);
						modal.setTitle("Edit Merchant Deal Offer");
						modal.show(target);
					}
				});

				sb.append("Are you sure you want to copy \"").append(title).append("\" and all deals related?");
				item.add(new ConfirmationIndicatingAjaxLink<Void>("copyLink",
						JavaScriptUtils.escapeQuotes(sb.toString()).toString())
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						try
						{
							taloolService.deepCopyDealOffer(dealOfferId);
							setResponsePage(DealOffersPage.class);
						}
						catch (ServiceException e)
						{
							LOG.error("Problem copying deal offer", e);
							SessionUtils.errorMessage("Problem copying deal offer '" + title);
						}

					}
				});
				
				final AJAXDownload download = new AJAXDownload()
				{

					private static final long serialVersionUID = 3028684784843907550L;

					@Override
					protected String getFileName() {
						return dealOffer.getTitle() + " Access Codes.txt";
					}

					@Override
					protected IResourceStream getResourceStream() {
						// download the new codes
		            	final int finalCount = downloadCodeCount;
			            IResourceStream resourceStream = new AbstractResourceStreamWriter()
						{
							private static final long serialVersionUID = 659665452240222410L;

							@Override
							public void write(OutputStream output)
							{
								try
								{
									PrintWriter writer = new PrintWriter(output);

									// TODO we should have a method that only returns the most recent X codes
									List<String> codes = taloolService.getActivationCodes(dealOfferId);
									int lastIndex = codes.size();
									List<String> recentCodes = codes.subList(lastIndex-finalCount, lastIndex);

									for (final String code : recentCodes)
									{
										writer.println(code);
									}

									writer.close();
								}
								catch (Exception e)
								{
									LOG.error("Problem writing codes: " + e.getLocalizedMessage(), e);
								}
							}

							@Override
							public String getContentType()
							{
								return "application/text";
							}
						};
						return resourceStream;
					}
					
				};
				item.add(download);
				
				item.add(new IndicatingAjaxLink<Void>("codeLink")
				{
					private static final long serialVersionUID = 268692101349122303L;
					private final int MAX_CODES_IN_ONE_CREATION = 10000;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						
						//Extract the code count parameter from RequestCycle
			            final Request request = RequestCycle.get().getRequest();
			            final String jsCodeCountString = request.getRequestParameters()
			                            .getParameterValue("codeCount").toString("");
			            
			            try
			            {
			            	downloadCodeCount = Integer.parseInt(jsCodeCountString);
			            	if (downloadCodeCount > MAX_CODES_IN_ONE_CREATION)
			            	{
			            		SessionUtils.errorMessage("Please enter a number smaller than "+MAX_CODES_IN_ONE_CREATION+".");
			            		downloadCodeCount = 0;
			            	}
			            }
			            catch (Exception e)
			            {
			            	SessionUtils.errorMessage("Please enter numbers only.");
			            	downloadCodeCount = 0;
			            }
			            
			            
			            if (downloadCodeCount > 0)
			            {
			            	// generate the codes and start the download
			            	try
							{
								taloolService.createActivationCodes(dealOfferId, downloadCodeCount);
								download.initiate(target);
								Session.get().success("Codes created and download started.");
							}
							catch (ServiceException e)
							{
								Session.get().error("Problem creating codes");
								LOG.error("Problem creating codes: " + e.getLocalizedMessage());
							}
			            }
			            
			            target.add(feedback);

					}
					
					@Override
					protected void updateAjaxAttributes(
							AjaxRequestAttributes attributes) {
						super.updateAjaxAttributes(attributes);
						
						List<CharSequence> urlArgumentMethods = attributes.getDynamicExtraParameters();
		                urlArgumentMethods.add("return {'codeCount': prompt('How many access codes would you like to generate?')};");
						
					}
					
					
				});

			}

		};

		add(customers);
	}

	@Override
	public String getHeaderTitle()
	{
		return "My Books";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		return new MerchantDealOfferPanel(contentId, m, callback);
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "Create New Book";
	}
	

}
