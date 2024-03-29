package com.talool.website.panel.merchant;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.FactoryManager;
import com.talool.core.MerchantAccount;
import com.talool.core.service.InvalidInputException;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantAccountListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.PropertiesPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.customer.definition.CustomerPurchaseDealOfferPanel;
import com.talool.website.panel.merchant.definition.MerchantAccountPanel;
import com.talool.website.panel.merchant.definition.MerchantAccountResetPasswordPanel;

public class MerchantAccountsPanel extends BaseTabPanel
{
	private static final Logger LOG = LoggerFactory.getLogger(MerchantAccountsPanel.class);
	private static final long serialVersionUID = 3634980968241854373L;
	private UUID _merchantId;

	public MerchantAccountsPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final BasePage page = (BasePage) getPage();

		final AdminModalWindow modalProps = new AdminModalWindow("modalProps");
		modalProps.setInitialWidth(650);
		add(modalProps.setOutputMarkupId(true));

		MerchantAccountListModel model = new MerchantAccountListModel();
		model.setMerchantId(_merchantId);
		final ListView<MerchantAccount> customers = new ListView<MerchantAccount>("accountRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantAccount> item)
			{

				final MerchantAccount account = item.getModelObject();
				final Long merchantaccountId = account.getId();

				item.setModel(new CompoundPropertyModel<MerchantAccount>(account));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
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
				
				
				final UUID customerId = getCustomerId(account);
				item.add(new AjaxLink<Void>("dealOfferLink")
				{

					private static final long serialVersionUID = 7381871678482983865L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						CustomerPurchaseDealOfferPanel panel = new CustomerPurchaseDealOfferPanel(
						modal.getContentId(), callback, customerId);
						modal.setContent(panel);
						modal.setTitle("Purchase Deal Offer");
						modal.show(target);
					}
				}.setVisible(customerId!=null));

				item.add(new AjaxLink<Void>("pwLink")
				{

					private static final long serialVersionUID = 8581489018535203283L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						MerchantAccountResetPasswordPanel panel = new MerchantAccountResetPasswordPanel(modal.getContentId(), callback,
								merchantaccountId);
						modal.setContent(panel);
						modal.setTitle("Reset Password");
						modal.show(target);
					}
				});

				item.add(new AjaxLink<Void>("editProps")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();

						PropertiesPanel panel = new PropertiesPanel(modalProps.getContentId(), Model.of(account.getProperties()),
								MerchantAccount.class)
						{

							private static final long serialVersionUID = -6061721033345142501L;

							@Override
							public void saveEntityProperties(Properties props, AjaxRequestTarget target)
							{
								try
								{
									ServiceFactory.get().getTaloolService().merge(account);
								}
								catch (ServiceException e)
								{
									LOG.error("Failed to merge account after editing properties", e);
								}
								target.appendJavaScript("window.parent.Wicket.Window.current.autoSizeWindow();");
							}

						};

						StringBuilder sb = new StringBuilder();
						modalProps.setContent(panel);
						sb.setLength(0);
						sb.append("Manage '").append(account.getEmail()).append("'").append(" properties");
						modalProps.setTitle(sb.toString());
						modalProps.show(target);
					}

				}.setVisible(page.isSuperUser));

			}

		};

		add(customers);
	}
	
	private UUID getCustomerId(MerchantAccount account)
	{
		UUID customerId = null;
		try
		{
			Customer c = FactoryManager.get()
					.getServiceFactory().getCustomerService().getCustomerByEmail(account.getEmail());
			if (c != null)
			{
				customerId = c.getId();
			}
		}
		catch (ServiceException se)
		{
			LOG.info("no user for merchant account with email: "+account.getEmail());
		} 
		catch (InvalidInputException e) {
			LOG.info("no user for merchant account with email: "+account.getEmail());
		}
		return customerId;
	}

	@Override
	public String getActionLabel()
	{
		return "Create Account";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return new MerchantAccountPanel(contentId, _merchantId, callback);
	}

}