package com.talool.website.pages;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.website.models.MerchantListModel;
import com.talool.website.panel.AdminMenuPanel;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantPage extends BasePage
{

	private static final long serialVersionUID = 9023714664854633955L;

	public MerchantPage()
	{
		super();
	}

	public MerchantPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));

		final ListView<Merchant> mechants = new ListView<Merchant>("merchRptr", new MerchantListModel())
		{

			private static final long serialVersionUID = 8844000843574646422L;

			@Override
			protected void populateItem(ListItem<Merchant> item)
			{
				Merchant merchant = item.getModelObject();

				item.setModel(new CompoundPropertyModel<Merchant>(merchant));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("name"));
				item.add(new Label("primaryLocation.address.niceCityState"));
				item.add(new Label("accounts", merchant.getNumberOfMerchantAccounts()));
			}

		};

		add(mechants);

	}
}
