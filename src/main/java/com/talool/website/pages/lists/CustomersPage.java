package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Customer;
import com.talool.website.models.CustomerListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.CustomerPanel;
import com.talool.website.panel.SubmitCallBack;

public class CustomersPage extends BasePage
{
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

		final AdminModalWindow customerModal;
		add(customerModal = new AdminModalWindow("modal"));

		final SubmitCallBack callback = new SubmitCallBack()
		{

			private static final long serialVersionUID = -3623444036634424930L;

			@Override
			public void submitSuccess(AjaxRequestTarget target)
			{
				customerModal.close(target);
				target.add(CustomersPage.this);
			}

			@Override
			public void submitFailure(AjaxRequestTarget target)
			{

			}
		};

		final CustomerPanel customerPanel = new CustomerPanel(customerModal.getContentId(), callback);
		customerModal.setContent(customerPanel);

		add(new AjaxLink<Void>("customerLink")
		{

			private static final long serialVersionUID = -150543962841890063L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				customerModal.setTitle("Create Customer");
				customerModal.setContent(new CustomerPanel(customerModal.getContentId(), callback));
				customerModal.show(target);
			}
		});

		final ListView<Customer> customers = new ListView<Customer>("customerRptr",
				new CustomerListModel())
		{

			private static final long serialVersionUID = 4104816505968727445L;

			@Override
			protected void populateItem(ListItem<Customer> item)
			{
				Customer customer = item.getModelObject();
				final Long customerId = customer.getId();

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

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = -4592149231430681542L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						CustomerPanel panel = new CustomerPanel(customerModal.getContentId(), callback,
								customerId);
						customerModal.setContent(panel);
						customerModal.setTitle("Edit Customer");
						customerModal.show(target);
					}
				});

				// TODO change the link to point to a deal offer purchase list
				PageParameters booksParams = new PageParameters();
				booksParams.set("method", "customer");
				booksParams.set("id", customer.getId());
				BookmarkablePageLink<Void> booksLink = new BookmarkablePageLink<Void>("booksLink",
						BooksPage.class, booksParams);
				item.add(booksLink);

				// TODO change the link to point to a deal acquire list
				PageParameters dealsParams = new PageParameters();
				dealsParams.set("method", "customer");
				dealsParams.set("id", customer.getId());
				BookmarkablePageLink<Void> dealsLink = new BookmarkablePageLink<Void>("dealsLink",
						DealsPage.class, dealsParams);
				item.add(dealsLink);

				PageParameters friendsParams = new PageParameters();
				friendsParams.set("id", customer.getId());
				BookmarkablePageLink<Void> friendsLink = new BookmarkablePageLink<Void>("friendsLink",
						FriendsPage.class, friendsParams);
				item.add(friendsLink);
			}

		};

		add(customers);
	}

}
