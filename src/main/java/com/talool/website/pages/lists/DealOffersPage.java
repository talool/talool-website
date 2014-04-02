package com.talool.website.pages.lists;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.stats.DealOfferSummary;
import com.talool.website.component.ConfirmationIndicatingAjaxLink;
import com.talool.website.models.DealOfferModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.DealOfferManagementPage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.dealoffer.DealOfferPublishToggle;
import com.talool.website.panel.dealoffer.wizard.DealOfferWizard;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class DealOffersPage extends BasePage
{
	private static final Logger LOG = Logger.getLogger(DealOffersPage.class);
	private static final long serialVersionUID = 2102415289760762365L;
	private static final String talool = "Talool";
	private static final String CONTAINER_ID = "bookList";
	private static final String REPEATER_ID = "offerRptr";
	private static final String NAVIGATOR_ID = "navigator";
	private int downloadCodeCount;
	private DealOfferWizard wizard;

	private String sortParameter = "title";
	private boolean isAscending = false;
	private int itemsPerPage = 50;
	private long itemCount;

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

		final WebMarkupContainer container = new WebMarkupContainer(CONTAINER_ID);
		container.setOutputMarkupId(true);
		add(container);

		WebMarkupContainer pubCol = new WebMarkupContainer("pubColHead");
		container.add(pubCol.setOutputMarkupId(true));
		pubCol.setVisible(isSuperUser);

		WebMarkupContainer bookTypeColHead = new WebMarkupContainer("bookTypeColHead");
		container.add(bookTypeColHead.setOutputMarkupId(true));
		bookTypeColHead.setVisible(isSuperUser);
		final DealOfferSummaryDataProvider dataProvider = new DealOfferSummaryDataProvider(sortParameter, isAscending);
		final DataView<DealOfferSummary> books = getDataView(dataProvider);
		container.add(books);

		// Set the labels above the pagination
		itemCount = dataProvider.size();
		container.add(new Label("totalCount", new PropertyModel<Long>(this, "itemCount")).setOutputMarkupId(true));

		final AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, books);
		container.add(pagingNavigator.setOutputMarkupId(true));
		pagingNavigator.setVisible(itemCount > itemsPerPage);

		container.add(new AjaxLink<Void>("titleLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("title", target);
			}
		});

		container.add(new AjaxLink<Void>("activeLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("isActive", target);
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
				MerchantAccount merchantAccount = SessionUtils.getSession().getMerchantAccount();
				final DealOffer offer = domainFactory.newDealOffer(merchantAccount.getMerchant(), merchantAccount);

				// set defaults
				offer.setDealType(DealType.PAID_BOOK);
				MerchantLocation offerLocation = offer.getMerchant().getPrimaryLocation();
				offer.setGeometry(offerLocation.getGeometry());

				wizard.setModelObject(offer);
				wizard.open(target);
			}
		};
		setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getNewDefinitionPanelTitle());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);

		// Wizard
		wizard = new DealOfferWizard("wiz", "Book Wizard")
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
	}

	private void resetPage(AjaxRequestTarget target)
	{
		// refresh the list after a book is edited
		final WebMarkupContainer container = (WebMarkupContainer) getPage().get(CONTAINER_ID);
		final DealOfferSummaryDataProvider provider = new DealOfferSummaryDataProvider(sortParameter, isAscending);
		final DataView<DealOfferSummary> dataView = getDataView(provider);
		container.replace(dataView);
		itemCount = provider.size();
		target.add(container);

		// replace the pagination
		final AjaxPagingNavigator pagingNavigator = getPagination(dataView);
		container.replace(pagingNavigator);
		target.add(pagingNavigator);
	}

	@SuppressWarnings("unchecked")
	private void doAjaxSearchRefresh(final String sortParam, final AjaxRequestTarget target)
	{
		WebMarkupContainer container = (WebMarkupContainer) getPage().get(CONTAINER_ID);

		final DataView<DealOfferSummary> dataView = ((DataView<DealOfferSummary>) container.get(REPEATER_ID));
		final DealOfferSummaryDataProvider provider = (DealOfferSummaryDataProvider) dataView.getDataProvider();

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

	private AjaxPagingNavigator getPagination(final DataView<DealOfferSummary> books)
	{
		AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, books);
		pagingNavigator.setOutputMarkupId(true);
		pagingNavigator.setVisible(itemCount > itemsPerPage);
		PagingNavigation nav = pagingNavigator.getPagingNavigation();
		if (nav != null)
		{
			nav.setViewSize(5);
		}
		return pagingNavigator;
	}

	private DataView<DealOfferSummary> getDataView(IDataProvider<DealOfferSummary> provider)
	{
		final StringBuilder sb = new StringBuilder();
		final DataView<DealOfferSummary> books = new DataView<DealOfferSummary>(REPEATER_ID, provider)
		{

			private static final long serialVersionUID = 4104816505968727445L;

			@Override
			protected void populateItem(Item<DealOfferSummary> item)
			{
				sb.setLength(0);
				final DealOfferSummary dealOffer = item.getModelObject();
				final UUID dealOfferId = dealOffer.getOfferId();
				final String title = dealOffer.getTitle();

				item.setModel(new CompoundPropertyModel<DealOfferSummary>(dealOffer));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				PageParameters dealsParams = new PageParameters();
				dealsParams.set("id", dealOfferId);
				dealsParams.set("name", dealOffer.getTitle());
				String url = (String) urlFor(DealOfferManagementPage.class, dealsParams);
				ExternalLink titleLink = new ExternalLink("titleLink", Model.of(url),
						new PropertyModel<String>(dealOffer, "title"));
				item.add(titleLink);

				// Publisher link. Only show it for Talool users
				WebMarkupContainer pubColCell = new WebMarkupContainer("pubColCell");
				item.add(pubColCell.setOutputMarkupId(true));
				pubColCell.setVisible(isSuperUser);
				PageParameters merchantParams = new PageParameters();
				merchantParams.set("id", dealOffer.getMerchantId());
				merchantParams.set("name", dealOffer.getMerchantName());
				String mUrl = (String) urlFor(MerchantManagementPage.class, merchantParams);
				ExternalLink merchantLink = new ExternalLink("merchantLink", Model.of(mUrl),
						new PropertyModel<String>(dealOffer, "merchantName"));
				pubColCell.add(merchantLink);

				String locationName = getGeoLocation(dealOffer);
				item.add(new Label("location", locationName));

				if (isKirkeBook(dealOffer))
				{
					item.add(new Label("price", ""));
				}
				else
				{
					NumberFormat formatter = NumberFormat.getCurrencyInstance();
					String moneyString = formatter.format(dealOffer.getPrice());
					item.add(new Label("price", moneyString));
				}

				final StringBuilder sb = new StringBuilder();

				item.add(new Label("merchantCount", dealOffer.getMerchantCount()));
				item.add(new Label("dealCount"));

				Label dealType = new Label("offerType");
				item.add(dealType.setOutputMarkupId(true));
				dealType.setVisible(isSuperUser);

				DateTimeFormatter dateformatter = DateTimeFormat.forPattern("MMM d, yyyy");
				DateTime startDate;
				
				if (dealOffer.getScheduledStartDate() == null)
				{
					startDate = new DateTime((new Date()).getTime());
				}
				else
				{
					startDate = new DateTime(dealOffer.getScheduledStartDate().getTime());
				}
				DateTime endDate;
				if (dealOffer.getScheduledEndDate() == null)
				{
					endDate = new DateTime((new Date()).getTime());
				}
				else
				{
					endDate = new DateTime(dealOffer.getScheduledEndDate().getTime());
				}
				
				sb.append(dateformatter.print(startDate)).append(" - ").append(dateformatter.print(endDate));
				item.add(new Label("schedule", sb.toString()));

				sb.setLength(0);

				item.add(new DealOfferPublishToggle("isActive", new Model<DealOfferSummary>(dealOffer)));

				sb.append("Are you sure you want to remove \"").append(title).append("\"?");
				ConfirmationIndicatingAjaxLink<Void> deleteLink = new ConfirmationIndicatingAjaxLink<Void>("deleteLink", JavaScriptUtils.escapeQuotes(
						sb.toString()).toString())
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						try
						{
							// Make "not active" and assign it to Talool.
							// This will hide it from the publisher without deleting the
							// offer.
							// If it has ever been active, we don't want to delete it because
							// we want to preserve the history.
							// TODO detected if the book was every sold, and delete if
							// possible
							DealOffer offer = taloolService.getDealOffer(dealOfferId);
							List<Merchant> merchants = taloolService.getMerchantByName(talool);
							offer.setMerchant(merchants.get(0));
							offer.setActive(false);
							taloolService.merge(offer);

							resetPage(target);

							Session.get().success(dealOffer.getTitle() + " has been sent back to Talool.  Contact us if you want it back.");
						}
						catch (ServiceException se)
						{
							LOG.error("problem fetcing the talool merchant id", se);
							Session.get().error("There was a problem removing " + dealOffer.getTitle() + ".  Contact us if you want it removed manually.");
						}
						target.add(feedback);

					}
				};
				item.add(deleteLink);

				AjaxLink<Void> editLink = new AjaxLink<Void>("editLink")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						wizard.setModelObject(new DealOfferModel(dealOfferId).getObject());
						wizard.open(target);
					}
				};

				item.add(editLink.setVisible(!isKirkeBook(dealOffer)));

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

			}

		};
		books.setItemsPerPage(itemsPerPage);
		return books;
	}

	private String getGeoLocation(DealOfferSummary offer)
	{
		String label = null;
		if (StringUtils.isEmpty(offer.getAddress1()))
		{
			label = "";
		}
		else if (!StringUtils.isEmpty(offer.getLocationName()))
		{
			label = offer.getLocationName();
		}
		else
		{
			StringBuilder locationLabel = new StringBuilder();
			locationLabel.append(offer.getAddress1());
			if (!StringUtils.isEmpty(offer.getAddress2()))
			{
				locationLabel.append(", ").append(offer.getAddress2());
			}
			locationLabel.append(", ").append(offer.getCity());
			locationLabel.append(", ").append(offer.getState());

			label = locationLabel.toString();
		}

		return label;
	}

	private boolean isKirkeBook(DealOfferSummary offer)
	{
		return offer.getOfferType().equals(DealType.KIRKE_BOOK.toString());
	}

	@Override
	public String getHeaderTitle()
	{
		return "Books";
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
		return "New Book";
	}

}
