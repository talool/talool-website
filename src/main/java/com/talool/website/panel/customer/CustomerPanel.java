package com.talool.website.panel.customer;

import java.util.Arrays;
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
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.Sex;
import com.talool.core.service.ServiceException;
import com.talool.website.models.CustomerModel;
import com.talool.website.panel.BasePanel;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.panel.SubmitCallBack;

public class CustomerPanel extends BasePanel {

	private static final long serialVersionUID = 2870193702212159884L;
	private static final Logger LOG = LoggerFactory.getLogger(CustomerPanel.class);
	
	private SubmitCallBack callback;
	
	private boolean isNew = false;
	
	private String confirm;

	public String getConfirm() {
		return confirm;
	}

	public CustomerPanel(String id, SubmitCallBack callback) {
		super(id);
		this.callback = callback;
		
		Customer customer = domainFactory.newCustomer();
		isNew = true;
		setDefaultModel(Model.of(customer));
	}
	
	public CustomerPanel(final String id, final SubmitCallBack callback, final Long customerId)
	{
		super(id);
		this.callback = callback;
		setDefaultModel(new CustomerModel(customerId));
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
		
		CompoundPropertyModel<Customer> cModel = new CompoundPropertyModel<Customer>(
				(IModel<Customer>) getDefaultModel());
		form.setDefaultModel(cModel);
		
		form.add(new TextField<String>("firstName").setRequired(true));
		form.add(new TextField<String>("lastName").setRequired(true));
		form.add(new TextField<String>("email").setRequired(true));
		// validate the passwords match
		FormComponent<String> pw1 = new PasswordTextField("password").setRequired(true);
		FormComponent<String> pw2 = new PasswordTextField("confirm",new PropertyModel<String>(this,"confirm")).setRequired(true);
		EqualPasswordInputValidator pwv = new EqualPasswordInputValidator(pw1,pw2);
		form.add(pw1);
		form.add(pw2);
		form.add(pwv);
		// format the date
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy",false);
		form.add(new DateTextField("birthDate", converter).setRequired(true));
		// radio buttons
		List<Sex> SEX = Arrays.asList(new Sex[] { Sex.Male, Sex.Female });
		form.add(new RadioChoice<Sex>("sex", SEX).setSuffix("").setRequired(true));
		
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
				StringBuilder sb = new StringBuilder();
				try
				{
					Customer customer = (Customer) form.getDefaultModelObject();
					taloolService.save(customer);
					target.add(feedback);
					sb.append("Successfully ").append(isNew ? "created '" : "updated '")
					.append(customer.getEmail()).append("'");
					getSession().info(sb.toString());
					callback.submitSuccess(target);
				}
				catch (ServiceException e)
				{
					sb.append("Problem saving customer: ").append(e.getLocalizedMessage());
					getSession().error(sb.toString());
					LOG.error(sb.toString());
					callback.submitFailure(target);
				}
			}

		});
	}

}
