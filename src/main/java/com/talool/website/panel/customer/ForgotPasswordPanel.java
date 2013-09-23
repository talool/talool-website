package com.talool.website.panel.customer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.EmailService;
import com.talool.core.service.InvalidInputException;
import com.talool.core.service.ServiceException;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.SessionUtils;

public class ForgotPasswordPanel extends Panel
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ForgotPasswordPanel.class);

	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();
	protected transient static final EmailService emailService = FactoryManager.get()
			.getServiceFactory().getEmailService();

	private String email;

	public ForgotPasswordPanel(String id)
	{
		super(id);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));

		final WebMarkupContainer container = new WebMarkupContainer("formContainer");
		add(container.setOutputMarkupId(true));

		Form<Void> form = new Form<Void>("forgot");
		container.add(form);
		form.setDefaultModel(new CompoundPropertyModel<ForgotPasswordPanel>(this));
		AjaxButton submit = new AjaxButton("submit", form)
		{

			private static final long serialVersionUID = -6562989540935949813L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				SessionUtils.errorMessage("Please enter a valid email");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				try
				{
					Customer customer = customerService.getCustomerByEmail(email);
					if (customer != null)
					{
						customerService.createPasswordReset(customer);
						emailService.sendPasswordRecoveryEmail(customer);
						SessionUtils.successMessage("An email has been sent with instructions for resetting your password.");
						target.add(container.setVisible(false));
						target.appendJavaScript("$('#page').trigger('create');");
					}
					else
					{
						SessionUtils.errorMessage("We couldn't find an account registered with that email address.");
					}
				}
				catch (ServiceException e)
				{
					SessionUtils.errorMessage("Unable to retrieve account: ", e.getLocalizedMessage());
					LOG.error(e.getLocalizedMessage(), e);
				}
				catch (InvalidInputException e)
				{
					SessionUtils.errorMessage(e.getMessage());
					LOG.error(e.getLocalizedMessage(), e);
				}
			}

		};
		form.add(submit);
		form.add(new EmailTextField("email").setRequired(true));
	}
}
