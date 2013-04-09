package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Deal;
import com.talool.website.models.DealListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class DealsPage extends BasePage {

	private static final long serialVersionUID = 1133795226226645331L;

	public DealsPage()
	{
		super();
	}

	public DealsPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
		
		DealListModel model = new DealListModel();
		model.setMerchantId(1);
		
		final ListView<Deal> deals = new ListView<Deal>("dealRptr", model)
		{

			private static final long serialVersionUID = -7055359730593625591L;

			@Override
			protected void populateItem(ListItem<Deal> item)
			{
				Deal deal = item.getModelObject();

				item.setModel(new CompoundPropertyModel<Deal>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("title"));
				item.add(new Label("summary"));
				item.add(new Label("isActive"));
				item.add(new Label("expires"));
			}

		};

		add(deals);
	}

}
