package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.website.models.MerchantListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;
import com.talool.website.panel.MerchantModalWindow;
import com.talool.website.panel.MerchantPanel;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.panel.SubmitCallBack;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantsPage extends BasePage
{
	private static final long serialVersionUID = 9023714664854633955L;

	public MerchantsPage()
	{
		super();
	}

	public MerchantsPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");

		add(feedback.setOutputMarkupId(true));

		final MerchantModalWindow merchantModal;
		add(merchantModal = new MerchantModalWindow("modal"));

		final SubmitCallBack callback = new SubmitCallBack()
		{
			private static final long serialVersionUID = -1459177645080455211L;

			@Override
			public void submitSuccess(AjaxRequestTarget target)
			{
				merchantModal.close(target);
				target.add(MerchantsPage.this);
			}

			@Override
			public void submitFailure(AjaxRequestTarget target)
			{

			}
		};

		final MerchantPanel merchantPanel = new MerchantPanel(merchantModal.getContentId(), callback);

		merchantModal.setContent(merchantPanel);

		add(new AjaxLink<Void>("merchantLink")
		{
			private static final long serialVersionUID = 8539856864609166L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				merchantModal.setTitle("Create Merchant");
				merchantModal.setContent(new MerchantPanel(merchantModal.getContentId(), callback));
				merchantModal.show(target);
			}
		});

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));

		final ListView<Merchant> mechants = new ListView<Merchant>("merchRptr", new MerchantListModel())
		{

			private static final long serialVersionUID = 8844000843574646422L;

			@Override
			protected void populateItem(ListItem<Merchant> item)
			{
				final Merchant merchant = item.getModelObject();

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

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantPanel panel = new MerchantPanel(merchantModal.getContentId(), callback,
								merchant);
						merchantModal.setContent(panel);
						merchantModal.setTitle("Edit Merchant");
						merchantModal.show(target);
					}
				});

				PageParameters booksParams = new PageParameters();
				booksParams.set("method", DealsPage.METHOD_MERCHANT);
				booksParams.set("id", merchant.getId());
				BookmarkablePageLink<Void> booksLink = new BookmarkablePageLink<Void>("booksLink",
						BooksPage.class, booksParams);
				item.add(booksLink);

				PageParameters dealsParams = new PageParameters();
				dealsParams.set("method", DealsPage.METHOD_MERCHANT);
				dealsParams.set("id", merchant.getId());
				BookmarkablePageLink<Void> dealsLink = new BookmarkablePageLink<Void>("dealsLink",
						DealsPage.class, dealsParams);
				item.add(dealsLink);
			}

		};

		add(mechants);

	}

	@Override
	protected void setHeaders(WebResponse response)
	{
		// TODO Auto-generated method stub
		super.setHeaders(response);
	}
}
