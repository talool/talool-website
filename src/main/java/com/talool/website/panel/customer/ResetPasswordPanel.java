package com.talool.website.panel.customer;

import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.SessionUtils;

public class ResetPasswordPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ResetPasswordPanel.class);
	private static final String RESET_CODE_ERR = "Your password reset link is not invalid.  Please issue another reset request";
	private static final String RESET_EXPIRED_ERR = "For your security, your password reset has expired.  Please issue another reset request";
	private static final String UNABLE_TO_RETRIEVE_ERR = "Unable to retrieve account";

	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();

	private UUID customer_id;
	private String password;
	private String confirm;
	private String passwordResetCode;

	public ResetPasswordPanel(String id, UUID c_id, String passwordResetCode)
	{
		super(id);
		customer_id = c_id;
		this.passwordResetCode = passwordResetCode;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		Customer customer = null;
		String errorMessage = null;

		try
		{
			customer = customerService.getCustomerById(customer_id);
			if (customer.getResetPasswordCode() == null || !customer.getResetPasswordCode().equals(passwordResetCode))
			{
				errorMessage = RESET_CODE_ERR;
			}
			else if (System.currentTimeMillis() > customer.getResetPasswordExpires().getTime())
			{
				errorMessage = RESET_EXPIRED_ERR;
			}

		}
		catch (ServiceException e)
		{
			LOG.error(e.getLocalizedMessage(), e);
			errorMessage = UNABLE_TO_RETRIEVE_ERR;
		}

		if (errorMessage != null)
		{
			SessionUtils.errorMessage(errorMessage);
		}

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));

		final WebMarkupContainer container = new WebMarkupContainer("formContainer");
		add(container.setOutputMarkupId(true).setVisible(errorMessage == null));

		Form<Void> form = new Form<Void>("forgot");
		container.add(form);
		form.setDefaultModel(new CompoundPropertyModel<ResetPasswordPanel>(this));

		AjaxButton submit = new AjaxButton("submit", form)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				SessionUtils.errorMessage("Please re-enter your password");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				try
				{
					Customer customer = customerService.getCustomerById(customer_id);
					if (customer != null)
					{
						customer.setPassword(password);
						customerService.save(customer);
						SessionUtils.successMessage("Your password has been updated.");
						target.add(container.setVisible(false));
						target.appendJavaScript("$('#page').trigger('create');");
					}
					else
					{
						SessionUtils.errorMessage("We couldn't find your account.");
					}
				}
				catch (ServiceException e)
				{
					SessionUtils.errorMessage(UNABLE_TO_RETRIEVE_ERR);
					LOG.error(e.getLocalizedMessage(), e);
				}
			}

		};
		form.add(submit);
		FormComponent<String> pw1 = new PasswordTextField("password").setRequired(true);
		FormComponent<String> pw2 = new PasswordTextField("confirm", new PropertyModel<String>(this,
				"confirm")).setRequired(true);
		EqualPasswordInputValidator pwv = new EqualPasswordInputValidator(pw1, pw2);
		form.add(pw1);
		form.add(pw2);
		form.add(pwv);
	}
}
