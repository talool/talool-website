package com.talool.website.panel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.Sex;
import com.talool.core.service.ServiceException;

public class CustomerPanel extends BasePanel {

	private static final long serialVersionUID = 2870193702212159884L;
	private static final Logger LOG = LoggerFactory.getLogger(CustomerPanel.class);
	
	private Customer customer = domainFactory.newCustomer();
	
	private SubmitCallBack callback;

	public CustomerPanel(String id, SubmitCallBack callback) {
		super(id);
		this.callback = callback;
	}
	
	public void setCustomer(Customer c)
	{
		customer = c;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));

		Form<Void> form = new Form<Void>("form");
		add(form);
		
		form.add(new TextField<String>("firstName", new PropertyModel<String>(customer, "firstName"))
				.setRequired(true));
		form.add(new TextField<String>("lastName", new PropertyModel<String>(customer, "lastName"))
				.setRequired(true));
		form.add(new TextField<String>("email", new PropertyModel<String>(customer, "email"))
				.setRequired(true));
		// validate the passwords match
		FormComponent<String> pw1 = new PasswordTextField("password", new PropertyModel<String>(customer, "password")).setRequired(true);
		FormComponent<String> pw2 = new PasswordTextField("confirm", new PropertyModel<String>(customer, "password")).setRequired(true);
		EqualPasswordInputValidator pwv = new EqualPasswordInputValidator(pw1,pw2);
		form.add(pw1);
		form.add(pw2);
		form.add(pwv);
		// format the date
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy",false);
		form.add(new DateTextField("birthDate", new PropertyModel<Date>(customer, "birthDate"), converter)
				.setRequired(true));
		// radio buttons
		List<Sex> SEX = Arrays.asList(new Sex[] { Sex.Male, Sex.Female });
		form.add(new RadioChoice<Sex>("groupSex",new PropertyModel<Sex>(customer, "sex"), SEX)
				.setSuffix("")
				.setRequired(true));
		
		form.add(new AjaxButton("submitButton", form)
		{

			private static final long serialVersionUID = 6751242687978273755L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				target.appendJavaScript("$('.content').scrollTop();");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				try
				{
					
					taloolService.save(customer);
					target.add(feedback);
					getSession().info("Successfully saved '" + customer.getEmail() + "'");
					callback.submitSuccess(target);
				}
				catch (ServiceException e)
				{
					String errMsg = "Problem saving customer: " + e.getLocalizedMessage();
					getSession().error(errMsg);
					LOG.error(errMsg);
					callback.submitFailure(target);
				}
			}

		});
	}

}
