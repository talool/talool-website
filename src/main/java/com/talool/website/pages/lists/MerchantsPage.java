package com.talool.website.pages.lists;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.gmap.GMap;

import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.stats.MerchantSummary;
import com.talool.website.component.ConfirmationIndicatingAjaxLink;
import com.talool.website.component.MerchantSearchPanel;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.image.EditableImage;
import com.talool.website.panel.merchant.wizard.MerchantWizard;
import com.talool.website.panel.merchant.wizard.MerchantWizard.MerchantWizardMode;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
@SecuredPage
public class MerchantsPage extends BasePage
{
	private static final long serialVersionUID = 9023714664854633955L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantsPage.class);
	private static final String talool = "Talool";
	private static final String CONTAINER_ID = "merchantList";
	private static final String REPEATER_ID = "merchRptr";
	private static final String NAVIGATOR_ID = "navigator";
	private static final String COUNT_ID = "merchantCount";
	private MerchantWizard wizard;
	
	private String sortParameter = "name";
	private boolean isAscending = true;
	private int itemsPerPage = 20;
	private long itemCount;
	
	private UUID searchMerchantId;

	public MerchantsPage()
	{
		super();
	}

	public MerchantsPage(PageParameters parameters)
	{
		super(parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final WebMarkupContainer container = new WebMarkupContainer(CONTAINER_ID);
		container.setOutputMarkupId(true);
		add(container);
		
		final MerchantSummaryDataProvider dataProvider = new MerchantSummaryDataProvider(sortParameter, isAscending);
		final DataView<MerchantSummary> merchants = getDataView(dataProvider);
		container.add(merchants);
		
		final MerchantSearchPanel searchPanel = new MerchantSearchPanel("searchPanel"){

			private static final long serialVersionUID = 1L;

			@Override
			public void onSearch(AjaxRequestTarget target, String merchantName) {
				WebMarkupContainer container = (WebMarkupContainer) getPage().get(CONTAINER_ID);

				final DataView<MerchantSummary> dataView = ((DataView<MerchantSummary>) container.get(REPEATER_ID));
				MerchantSummaryDataProvider provider = (MerchantSummaryDataProvider) dataView.getDataProvider();
				provider.setTitle(merchantName);

				final AjaxPagingNavigator pagingNavigator = (AjaxPagingNavigator) container.get(NAVIGATOR_ID);
				pagingNavigator.getPageable().setCurrentPage(0);
				
				itemCount = provider.size();
				pagingNavigator.setVisible(itemCount > itemsPerPage);
				
				target.add(container);
				target.add(pagingNavigator);
			}
			
		};
		container.add(searchPanel);
		
		itemCount = dataProvider.size();
		container.add(new Label(COUNT_ID,new PropertyModel<Long>(this, "itemCount")));
		
		final AjaxPagingNavigator pagingNavigator = getPagination(merchants);
		container.add(pagingNavigator.setOutputMarkupId(true));
		PagingNavigation nav = pagingNavigator.getPagingNavigation();
		if (nav != null)
		{
			nav.setViewSize(5);
		}
		
		container.add(new AjaxLink<Void>("categoryLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("category", target);
			}
		});

		container.add(new AjaxLink<Void>("nameLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("name", target);
			}
		});

		// override the action button
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				final Merchant merchant = domainFactory.newMerchant();

				merchant.getLocations().iterator().next().
						setCreatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());
				wizard.setModelObject(merchant);
				wizard.open(target);
			}
		};
		setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getNewDefinitionPanelTitle());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);

		// Wizard
		wizard = new MerchantWizard("wiz", "Merchant Wizard", MerchantWizardMode.MERCHANT)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				
				resetPage(target);
				
				target.add(feedback);
			}
		};
		add(wizard);

		// preload the map to avoid a race condition with the loading of js
		// dependencies
		GMap map = new GMap("preloadMap");
		add(map);

	}
	
	private AjaxPagingNavigator getPagination(final DataView<MerchantSummary> merchants)
	{
		AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, merchants);
		pagingNavigator.setOutputMarkupId(true);
		pagingNavigator.setVisible(itemCount > itemsPerPage);
		return pagingNavigator;
	}
	
	private DataView<MerchantSummary> getDataView(IDataProvider<MerchantSummary> dataProvider)
	{
		final DataView<MerchantSummary> merchants = new DataView<MerchantSummary>(REPEATER_ID,dataProvider)
		{

			private static final long serialVersionUID = 8844000843574646422L;

			@Override
			protected void populateItem(Item<MerchantSummary> item)
			{
				MerchantSummary merchant = item.getModelObject();
				final UUID merchantId = merchant.getMerchantId();

				item.setModel(new CompoundPropertyModel<MerchantSummary>(merchant));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				PageParameters booksParams = new PageParameters();
				booksParams.set("id", merchantId);
				booksParams.set("name", merchant.getName());
				String url = (String) urlFor(MerchantManagementPage.class, booksParams);
				ExternalLink namelLink = new ExternalLink("nameLink", Model.of(url),
						new PropertyModel<String>(merchant, "name"));

				item.add(namelLink);
				item.add(new Label("category"));
				item.add(new Label("address1"));
				item.add(new Label("address2"));
				
				item.add(new Label("city"));
				item.add(new Label("state"));
				item.add(new Label("zip"));
				
				item.add(new EditableImage("editableImage",Model.of(merchant.getImageUrl()), merchantId, MediaType.MERCHANT_IMAGE)
				{

					private static final long serialVersionUID = 1L;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						try
						{
							Merchant m = taloolService.getMerchantById(merchantId);
							m.getCurrentLocation().setMerchantImage(media);
							taloolService.merge(m.getCurrentLocation());
							target.add(this);
						}
						catch (ServiceException se)
						{
							LOG.error("Failed to save new image with merchant",se);
						}
						
					}
					
				});
				
				item.add(new EditableImage("editableLogo",Model.of(merchant.getLogoUrl()), merchantId, MediaType.MERCHANT_LOGO)
				{

					private static final long serialVersionUID = -7677598504543286741L;
					private int iframeWidth = 100;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) 
					{
						try
						{
							// update the current locations
							Merchant m = taloolService.getMerchantById(merchantId);
							m.getCurrentLocation().setLogo(media);
							taloolService.merge(m.getCurrentLocation());
							
							// check for other locations that have no logo and update them too
							for (MerchantLocation loc:m.getLocations())
							{
								if (loc.getLogo() == null)
								{
									loc.setLogo(media);
									taloolService.merge(loc);
								}
							}
							
							target.add(this);
						}
						catch (ServiceException se)
						{
							LOG.error("Failed to save new logo with merchant",se);
						}
						
					}

					@Override
					public int getIframeWidth() {
						return iframeWidth;
					}
				});

				StringBuilder hasMultiple = new StringBuilder();
				hasMultiple.append(merchant.getLocationCount());
				item.add(new Label("multiple", hasMultiple.toString()));

				// TODO - at some point, this tags label can be based on a model
				// item.add(new Label("tags", ModelUtil.geTagSummary(merchant)));

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						wizard.setModelObject(new MerchantModel(merchantId, true).getObject());
						wizard.open(target);
					}
				});
				
				StringBuilder confirm = new StringBuilder();
				confirm.append("Are you sure you want to remove \"").append(merchant.getName()).append("\"?");
				ConfirmationIndicatingAjaxLink<Void> deleteLink = new ConfirmationIndicatingAjaxLink<Void>("deleteLink", JavaScriptUtils.escapeQuotes(confirm.toString()).toString())
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						
						try 
						{
							// Assign it to Talool and make it not discoverable. 
							// This will hide it from the publisher without deleting the merchant.
							// If it has ever had active deals, we don't want to delete it because 
							// we want to preserve the history.
							// TODO detected if the merchant was every sold anything, and delete if possible
							Merchant merch = taloolService.getMerchantById(merchantId);
							List<Merchant> merchants = taloolService.getMerchantByName(talool);
							Merchant _talool = merchants.get(0);
							MerchantAccount _taloolAccount = _talool.getMerchantAccounts().iterator().next();
							for (MerchantLocation loc : merch.getLocations())
							{
								loc.setCreatedByMerchantAccount(_taloolAccount);
							}
							merch.setIsDiscoverable(false);
							taloolService.merge(merch);
							
							resetPage(target);
							
							Session.get().success(merch.getName() + " has been sent back to Talool.  Contact us if you want it back.");
						} 
						catch (ServiceException se)
						{
							LOG.error("problem fetcing the talool merchant id", se);
							Session.get().error("There was a problem removing this merchant.  Contact us if you want it removed manually.");
						}
						target.add(feedback);
						
					}
				};
				item.add(deleteLink);

			}

		};
		merchants.setItemsPerPage(itemsPerPage);
		
		return merchants;
	}
	
	@SuppressWarnings("unchecked")
	private void doAjaxSearchRefresh(final String sortParam, final AjaxRequestTarget target)
	{
		WebMarkupContainer container = (WebMarkupContainer) getPage().get(CONTAINER_ID);

		final DataView<MerchantSummary> dataView = ((DataView<MerchantSummary>) container.get(REPEATER_ID));
		final MerchantSummaryDataProvider provider = (MerchantSummaryDataProvider) dataView.getDataProvider();

		// toggle asc/desc
		if (sortParam.equals(sortParameter))
		{
			isAscending = isAscending == true ? false : true;
			provider.setAscending(isAscending);
		}

		this.sortParameter = sortParam;

		provider.setSortParameter(sortParam);

		final AjaxPagingNavigator pagingNavigator = (AjaxPagingNavigator) container.get(NAVIGATOR_ID);
		pagingNavigator.getPageable().setCurrentPage(0);

		target.add(container);
		target.add(pagingNavigator);

	}
	
	private void resetPage(AjaxRequestTarget target)
	{
		// refresh the list after a book is edited
		final WebMarkupContainer container = (WebMarkupContainer) getPage().get(CONTAINER_ID);
		final MerchantSummaryDataProvider provider = new MerchantSummaryDataProvider(sortParameter, isAscending);
		final DataView<MerchantSummary> dataView = getDataView(provider);
		container.replace(dataView);
		itemCount = provider.size();
		target.add(container);
		
		// replace the pagination
		final AjaxPagingNavigator pagingNavigator = getPagination(dataView);
		container.replace(pagingNavigator);
		target.add(pagingNavigator);
		
		PagingNavigation nav = pagingNavigator.getPagingNavigation();
		if (nav != null)
		{
			nav.setViewSize(5);
		}
	}

	@Override
	protected void setHeaders(WebResponse response)
	{
		super.setHeaders(response);
	}

	@Override
	public String getHeaderTitle()
	{
		return "Merchants";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		// intentionally null
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "New Merchant";
	}
}
