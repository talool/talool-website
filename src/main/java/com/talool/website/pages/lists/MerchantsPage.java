package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.website.Config;
import com.talool.website.models.MerchantListModel;
import com.talool.website.models.ModelUtil;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.MerchantPanel;

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
		Config.get().getUploadDir();

		final AdminModalWindow merchantModal;
		add(merchantModal = new AdminModalWindow("modal"));

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

		final ListView<Merchant> mechants = new ListView<Merchant>("merchRptr", new MerchantListModel())
		{

			private static final long serialVersionUID = 8844000843574646422L;

			@Override
			protected void populateItem(ListItem<Merchant> item)
			{
				Merchant merchant = item.getModelObject();
				final Long merchantId = merchant.getId();

				item.setModel(new CompoundPropertyModel<Merchant>(merchant));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				// item.add(new Label("name"));

				PageParameters booksParams = new PageParameters();
				booksParams.set("id", merchant.getId());
				booksParams.set("name", merchant.getName());

				String url = (String) urlFor(MerchantManagementPage.class, booksParams);
				ExternalLink namelLink = new ExternalLink("nameLink", Model.of(url),
						new PropertyModel<String>(merchant, "name"));

				item.add(namelLink);

				item.add(new Label("primaryLocation.address.niceCityState"));
				item.add(new Label("accounts", merchant.getNumberOfMerchantAccounts()));

				// TODO - at some point, this tags label can be based on a model
				item.add(new Label("tags", ModelUtil.geTagSummary(merchant)));

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantPanel panel = new MerchantPanel(merchantModal.getContentId(), callback,
								merchantId);
						merchantModal.setContent(panel);
						merchantModal.setTitle("Edit Merchant");
						merchantModal.show(target);
					}
				});

			}

		};

		add(mechants);

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
}
