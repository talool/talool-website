package com.talool.website.panel.merchant;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.MerchantAccount;
import com.talool.website.models.MerchantAccountListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantAccountPanel;

public class MerchantAccountsPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 3634980968241854373L;
	private Long _merchantId;

	public MerchantAccountsPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = parameters.get("id").toLongObject();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		MerchantAccountListModel model = new MerchantAccountListModel();
		model.setMerchantId(_merchantId);
		final ListView<MerchantAccount> customers = new ListView<MerchantAccount>("accountRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantAccount> item)
			{
				
				MerchantAccount account = item.getModelObject();
				final Long merchantaccountId = account.getId();

				item.setModel(new CompoundPropertyModel<MerchantAccount>(account));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "gray0-bg"));
				}

				item.add(new Label("roleTitle"));
				item.add(new Label("email"));
				item.add(new Label("allowDealCreation"));

				BasePage page = (BasePage) this.getPage();
				final AdminModalWindow modal = page.getModal();
				final SubmitCallBack callback = page.getCallback(modal);
				item.add(new AjaxLink<Void>("editLink")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantAccountPanel panel = new MerchantAccountPanel(modal.getContentId(), callback, merchantaccountId);
						modal.setContent(panel);
						modal.setTitle("Edit Merchant Account");
						modal.show(target);
					}
				});
			}

		};

		add(customers);
	}

	@Override
	public String getActionLabel() {
		return "Create Merchant Account";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		return new MerchantAccountPanel(contentId, _merchantId, callback);
	}


}