package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.stats.MerchantCodeSummary;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.lists.MerchantCodeSummaryDataProvider;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;

public class FundraiserTrackingPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 522375793977325630L;

	private UUID _fundraiserId;
	private UUID _publisherId;
	
	private static final String CONTAINER_ID = "list";
	private static final String REPEATER_ID = "codeRepeater";
	private static final String NAVIGATOR_ID = "navigator";
	
	private String sortParameter = "name";
	private boolean isAscending = true;
	private int itemsPerPage = 50;
	private long itemCount;

	public FundraiserTrackingPanel(String id, PageParameters parameters)
	{
		super(id);
		_fundraiserId = UUID.fromString(parameters.get("id").toString());
		_publisherId = UUID.fromString(parameters.get("pid").toString());
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

		// hide the action button
		final BasePage page = (BasePage) this.getPage();
		page.getActionLink().add(new AttributeModifier("class", "hide"));
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
				item.add(new Label("code"));
				item.add(new Label("email"));
				item.add(new Label("purchaseCount"));

			}

		};
		codes.setItemsPerPage(itemsPerPage);
		return codes;
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
