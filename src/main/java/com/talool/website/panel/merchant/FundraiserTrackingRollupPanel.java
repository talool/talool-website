package com.talool.website.panel.merchant;

import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.stats.MerchantCodeSummary;
import com.talool.website.marketing.pages.FundraiserInstructions;
import com.talool.website.marketing.pages.FundraiserTracking;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.lists.MerchantCodeSummaryDataProvider;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.TrackingCodeMetaPanel;
import com.talool.website.util.PublisherCobrand;
import com.talool.website.util.SessionUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.UUID;

public class FundraiserTrackingRollupPanel extends Panel
{
	private static final Logger LOG = Logger.getLogger(FundraiserTrackingRollupPanel.class);
	private static final long serialVersionUID = -2904803042012425085L;
	private UUID _fundraiserId;
	private UUID _publisherId;
	
	private static final String CONTAINER_ID = "list";
	private static final String REPEATER_ID = "codeRepeater";
	private static final String NAVIGATOR_ID = "navigator";
	
	private String sortParameter = "name";
	private PublisherCobrand cobrand;
	private boolean isAscending = true;
	private int itemsPerPage = 50;
	private long itemCount;
	private boolean isAdmin = false;

	public FundraiserTrackingRollupPanel(String id, PageParameters parameters, boolean isAdmin)
	{
		super(id);
		_fundraiserId = UUID.fromString(parameters.get("id").toString());
		_publisherId = UUID.fromString(parameters.get("pid").toString());
		this.isAdmin = isAdmin;
		cobrand = new PublisherCobrand(_publisherId);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final WebMarkupContainer container = new WebMarkupContainer(CONTAINER_ID);
		container.setOutputMarkupId(true);
		add(container);
		
		final MerchantCodeSummaryDataProvider dataProvider = new MerchantCodeSummaryDataProvider(_fundraiserId, sortParameter, isAscending);
		final DataView<MerchantCodeSummary> deals = getDataView(dataProvider);
		container.add(deals);
		
		// Set the labels above the pagination
		itemCount = deals.getItemCount();
		Label totalCount = new Label("totalCount",new PropertyModel<Long>(this, "itemCount"));
		container.add(totalCount.setOutputMarkupId(true));
		
		AjaxLink<Void> createCode = new AjaxLink<Void>("genericCode")
		{

			private static final long serialVersionUID = 8250052316939936932L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// generate the code
				try
				{
					MerchantAccount merchantAccount = SessionUtils.getSession().getMerchantAccount();
					String title = " ";
					String notes = "Generic Tracking Code";
					Merchant f = (new MerchantModel(_fundraiserId, true)).getObject();
					ServiceFactory.get().getTaloolService().createMerchantCodeGroup(f,
							merchantAccount.getId(), _publisherId, title, notes, (short)1);

					Session.get().success("Generic tracking code created.");
					
					if (isAdmin)
					{
						BasePage page = (BasePage)getPage();
						target.add(page.feedback);
					}
					
					target.add(container);
					MerchantCodeSummaryDataProvider dataProvider = 
							new MerchantCodeSummaryDataProvider(_fundraiserId, sortParameter, isAscending);
					DataView<MerchantCodeSummary> deals = getDataView(dataProvider);
					itemCount = deals.getItemCount();
					container.addOrReplace(deals);
				}
				catch (ServiceException e)
				{
					Session.get().error("Problem creating codes");
					LOG.error("Problem creating codes: " + e.getLocalizedMessage());
				}
			}
		};
		container.add(createCode.setVisible(isAdmin));
		
		String actionsLabel = (isAdmin)?"Actions":"";
		container.add(new Label("actionsContainer",actionsLabel));
				
		final AjaxPagingNavigator pagingNavigator = new AjaxPagingNavigator(NAVIGATOR_ID, deals);
		container.add(pagingNavigator.setOutputMarkupId(true));
		pagingNavigator.setVisible(dataProvider.size() > itemsPerPage);
		pagingNavigator.getPagingNavigation().setViewSize(5);

		container.add(new AjaxLink<Void>("nameSortLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("name", target);
			}
		});
		
		container.add(new AjaxLink<Void>("codeSortLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("code", target);
			}
		});
		
		container.add(new AjaxLink<Void>("emailSortLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("email", target);
			}
		});
		
		container.add(new AjaxLink<Void>("countSortLink")
		{
			private static final long serialVersionUID = -4528179721619677443L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				doAjaxSearchRefresh("purchaseCount", target);
			}
		});

	}
	
	private DataView<MerchantCodeSummary> getDataView(IDataProvider<MerchantCodeSummary> dataProvider)
	{
		
		final DataView<MerchantCodeSummary> codes = new DataView<MerchantCodeSummary>(REPEATER_ID,dataProvider)
		{

			private static final long serialVersionUID = 2705519558987278333L;

			@Override
			protected void populateItem(Item<MerchantCodeSummary> item)
			{
				final MerchantCodeSummary code = item.getModelObject();
				item.setModel(new CompoundPropertyModel<MerchantCodeSummary>(code));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("name"));

				String cobrandFiller = "fundraiser"; // extra param that isn't being used
				
				PageParameters codeParams = new PageParameters();
				codeParams.set(0, cobrand.getCobrandName());
				codeParams.set(1, cobrandFiller);
				codeParams.set(2, code.getCode());
				String url = (String) urlFor(FundraiserTracking.class, codeParams);
				ExternalLink codeLink = new ExternalLink("codeLink", Model.of(url),
						new PropertyModel<String>(code, "code"));
				item.add(codeLink);
				
				item.add(new Label("email"));
				item.add(new Label("purchaseCount"));
				
				PageParameters pageParameters = new PageParameters();
				pageParameters.set("merchant",cobrand.getCobrandName());
				pageParameters.set("cobrand",cobrandFiller);
				pageParameters.set("code",code.getCode());
				item.add(new BookmarkablePageLink<String>("helpLink",FundraiserInstructions.class, pageParameters).setVisible(isAdmin));

				item.add(new AjaxLink<Void>("editProps")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						
						showModal(target, code.getCode());
					}

				}.setVisible(isAdmin));

			}

		};
		codes.setItemsPerPage(itemsPerPage);
		return codes;
	}
	
	private void showModal(AjaxRequestTarget target, String code)
	{
		BasePage page = (BasePage)getPage();
		final AdminModalWindow definitionModal = page.getModal();
		final SubmitCallBack callback = page.getCallback(definitionModal);
		
		TrackingCodeMetaPanel panel = new TrackingCodeMetaPanel(
				definitionModal.getContentId(), callback, code);
		definitionModal.setContent(panel);
		definitionModal.setTitle("Tracking Code: "+code);
		definitionModal.show(target);
		
	}
	
	@SuppressWarnings("unchecked")
	private void doAjaxSearchRefresh(final String sortParam, final AjaxRequestTarget target)
	{
		WebMarkupContainer container = (WebMarkupContainer) get(CONTAINER_ID);

		final DataView<MerchantCodeSummary> dataView = ((DataView<MerchantCodeSummary>) container.get(REPEATER_ID));
		final MerchantCodeSummaryDataProvider provider = (MerchantCodeSummaryDataProvider) dataView.getDataProvider();

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

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

}
