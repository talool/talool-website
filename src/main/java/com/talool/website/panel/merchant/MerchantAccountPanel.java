package com.talool.website.panel.merchant;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantAccountModel;
import com.talool.website.panel.BasePanel;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.panel.SubmitCallBack;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantAccountPanel extends BasePanel
{
	private static final long serialVersionUID = 7730915834242821975L;

	private static final Logger LOG = LoggerFactory.getLogger(MerchantAccountPanel.class);

	private SubmitCallBack callback;

	private boolean isNew = false;
	
	private String confirm;

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public MerchantAccountPanel(final String id, final Long merchantId, final SubmitCallBack callback)
	{
		super(id);
		this.callback = callback;
		
		Merchant merchant = null;
		try {
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(merchantId);
		} catch (ServiceException se) {
			LOG.error("problem loading merchant", se);
		}

		MerchantAccount account = domainFactory.newMerchantAccount(merchant);
		isNew = true;
		setDefaultModel(Model.of(account));
		
	}

	public MerchantAccountPanel(final String id, final SubmitCallBack callback, final Long merchantAccountId)
	{
		super(id);
		this.callback = callback;
		setDefaultModel(new MerchantAccountModel(merchantAccountId));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));
		
		Form<Void> form = new Form<Void>("form");
		add(form);

		CompoundPropertyModel<MerchantAccount> merchAccModel = new CompoundPropertyModel<MerchantAccount>(
				(IModel<MerchantAccount>) getDefaultModel());
		form.setDefaultModel(merchAccModel);

		form.add(new AjaxButton("submitButton", form)
		{
			private static final long serialVersionUID = -8745175531325052950L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				// attempting to scroll to top
				target.appendJavaScript("$('.content').scrollTop();");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				StringBuilder sb = new StringBuilder();

				try
				{

					MerchantAccount account = (MerchantAccount) form.getDefaultModelObject();
					
					taloolService.save(account);
					target.add(feedback);
					sb.append("Successfully ").append(isNew ? "created '" : "updated '")
							.append(account.getEmail()).append("'");
					getSession().info(sb.toString());
					callback.submitSuccess(target);
				}
				catch (ServiceException e)
				{
					sb.append("Problem saving account: ").append(e.getLocalizedMessage());
					getSession().error(sb.toString());
					LOG.error(sb.toString());
					callback.submitFailure(target);
				}
			}

		});

		form.add(new TextField<String>("roleTitle").setRequired(true));
		form.add(new TextField<String>("email").setRequired(true));
		// validate the passwords match
		FormComponent<String> pw1 = new PasswordTextField("password").setRequired(true);
		FormComponent<String> pw2 = new PasswordTextField("confirm",new PropertyModel<String>(this,"confirm")).setRequired(true);
		EqualPasswordInputValidator pwv = new EqualPasswordInputValidator(pw1,pw2);
		form.add(pw1);
		form.add(pw2);
		form.add(pwv);
		form.add(new CheckBox("allowDealCreation"));

	}

}
