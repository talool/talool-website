package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.stats.MerchantCodeSummary;
import com.talool.website.models.MerchantCodeSummaryListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;

public class FundraiserTrackingPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 522375793977325630L;

	private UUID _fundraiserId;
	private UUID _publisherId;

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

		// add a repeater
		MerchantCodeSummaryListModel model = new MerchantCodeSummaryListModel();
		model.setMerchantId(_fundraiserId);
		
		final WebMarkupContainer container = new WebMarkupContainer("list");
		container.setOutputMarkupId(true);
		add(container);

		final ListView<MerchantCodeSummary> codes = new ListView<MerchantCodeSummary>(
				"codeRepeater", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantCodeSummary> item)
			{
				final MerchantCodeSummary codeGroup = item.getModelObject();

				item.setModel(new CompoundPropertyModel<MerchantCodeSummary>(codeGroup));

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
		container.add(codes);

		// hide the action button
		final BasePage page = (BasePage) this.getPage();
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

}
