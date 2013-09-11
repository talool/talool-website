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
import com.talool.core.service.EmailService;
import com.talool.core.service.ServiceException;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.SessionUtils;

public class ResetPasswordPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ResetPasswordPanel.class);
	
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();
	
	private UUID customer_id;
	private String password;
	private String confirm;
	
	public ResetPasswordPanel(String id, UUID c_id) {
		super(id);
		customer_id = c_id;
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));
		
		final WebMarkupContainer container = new WebMarkupContainer("formContainer");
		add(container.setOutputMarkupId(true));
		
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
						
					}
					else
					{
						SessionUtils.errorMessage("We couldn't find your account.");
					}
				}
				catch (ServiceException e)
				{
					SessionUtils.errorMessage("Unable to retrieve account: ", e.getLocalizedMessage());
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
