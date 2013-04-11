package com.talool.website.pages.lists.merchant;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantAccountListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.MerchantAccountPanel;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.panel.SubmitCallBack;

public class AccountsPage extends BasePage {
	
	private static final long serialVersionUID = 3634980968241854373L;
	private static final Logger LOG = LoggerFactory.getLogger(AccountsPage.class);
	private Long _merchantId;

	public AccountsPage(PageParameters parameters)
	{
		super(parameters);
		_merchantId = parameters.get("id").toLongObject();
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
		
		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));
		
		MerchantAccountListModel model = new MerchantAccountListModel();
		StringBuffer pageTitle = new StringBuffer("Merchant Accounts for ");
		Merchant merchant = null;
		try {
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(_merchantId);
			pageTitle.append(merchant.getName());
			model.setMerchantId(merchant.getId());
		} catch (ServiceException se) {
			LOG.error("problem loading merchant", se);
		}
		add(new Label("pageTitle",pageTitle.toString()));
		
		final AdminModalWindow accountModal;
		add(accountModal = new AdminModalWindow("modal"));
		final SubmitCallBack callback = new SubmitCallBack()
		{

			private static final long serialVersionUID = 6420614586937543567L;

			@Override
			public void submitSuccess(AjaxRequestTarget target)
			{
				accountModal.close(target);
				target.add(AccountsPage.this);
			}

			@Override
			public void submitFailure(AjaxRequestTarget target)
			{

			}
		};

		final MerchantAccountPanel accountPanel = new MerchantAccountPanel(accountModal.getContentId(), _merchantId, callback);
		accountModal.setContent(accountPanel);
		add(new AjaxLink<Void>("accountLink")
		{

			private static final long serialVersionUID = 7891264295227523725L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				accountModal.setTitle("Create Merchant Account");
				accountModal.setContent(new MerchantAccountPanel(accountModal.getContentId(), _merchantId, callback));
				accountModal.show(target);
			}
		});
		
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
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("roleTitle"));
				item.add(new Label("email"));
				item.add(new Label("allowDealCreation"));
				
				item.add(new AjaxLink<Void>("editLink")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantAccountPanel panel = new MerchantAccountPanel(accountModal.getContentId(), callback,
								merchantaccountId);
						accountModal.setContent(panel);
						accountModal.setTitle("Edit Merchant Account");
						accountModal.show(target);
					}
				});
			}

		};

		add(customers);
	}

}