package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Customer;
import com.talool.website.models.CustomerListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.define.CustomerPage;
import com.talool.website.panel.AdminMenuPanel;

public class CustomersPage extends BasePage {
	
	private static final long serialVersionUID = 2102415289760762365L;

	public CustomersPage()
	{
		super();
	}

	public CustomersPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
		
		final ListView<Customer> customers = new ListView<Customer>("customerRptr", new CustomerListModel())
		{

			private static final long serialVersionUID = 4104816505968727445L;

			@Override
			protected void populateItem(ListItem<Customer> item)
			{
				Customer customer = item.getModelObject();

				item.setModel(new CompoundPropertyModel<Customer>(customer));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("firstName"));
				item.add(new Label("lastName"));
				item.add(new Label("email"));
				
				PageParameters editParams = new PageParameters();
				editParams.set("id", customer.getId());
				BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink",CustomerPage.class,editParams);
				item.add(editLink);
				
				// TODO change the link to point to a deal offer purchase list
				PageParameters booksParams = new PageParameters();
				booksParams.set("method", "customer");
				booksParams.set("id", customer.getId());
				BookmarkablePageLink<Void> booksLink = new BookmarkablePageLink<Void>("booksLink",BooksPage.class,booksParams);
				item.add(booksLink);
				
				// TODO change the link to point to a deal acquire list
				PageParameters dealsParams = new PageParameters();
				dealsParams.set("method", "customer");
				dealsParams.set("id", customer.getId());
				BookmarkablePageLink<Void> dealsLink = new BookmarkablePageLink<Void>("dealsLink",DealsPage.class,dealsParams);
				item.add(dealsLink);
				
				PageParameters friendsParams = new PageParameters();
				friendsParams.set("id", customer.getId());
				BookmarkablePageLink<Void> friendsLink = new BookmarkablePageLink<Void>("friendsLink",FriendsPage.class,friendsParams);
				item.add(friendsLink);
			}

		};

		add(customers);
	}

}
