package com.talool.website.panel.customer.definition;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Customer;
import com.talool.core.Sex;
import com.talool.core.service.ServiceException;
import com.talool.website.models.CustomerModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

public class CustomerPanel extends BaseDefinitionPanel
{

	private static final long serialVersionUID = 2870193702212159884L;

	public CustomerPanel(String id, SubmitCallBack callback)
	{
		super(id, callback);

		Customer customer = domainFactory.newCustomer();
		setDefaultModel(Model.of(customer));
	}

	public CustomerPanel(final String id, final SubmitCallBack callback, final UUID customerId)
	{
		super(id, callback);
		setDefaultModel(new CustomerModel(customerId));
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		form.add(new TextField<String>("firstName").setRequired(true));
		form.add(new TextField<String>("lastName").setRequired(true));
		form.add(new TextField<String>("email").setRequired(true));
		// validate the passwords match
		FormComponent<String> pw1 = new PasswordTextField("password").setRequired(true);
		FormComponent<String> pw2 = new PasswordTextField("confirm", new PropertyModel<String>(this,
				"confirm")).setRequired(true);
		EqualPasswordInputValidator pwv = new EqualPasswordInputValidator(pw1, pw2);
		form.add(pw1);
		form.add(pw2);
		form.add(pwv);
		// format the date
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		form.add(new DateTextField("birthDate", converter).setRequired(true));
		// radio buttons
		List<Sex> SEX = Arrays.asList(new Sex[] { Sex.Male, Sex.Female });
		form.add(new RadioChoice<Sex>("sex", SEX).setSuffix("").setRequired(true));

	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<Customer> getDefaultCompoundPropertyModel()
	{
		return new CompoundPropertyModel<Customer>((IModel<Customer>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier()
	{
		final Customer customer = (Customer) form.getDefaultModelObject();
		return customer.getEmail();
	}

	@Override
	public void save() throws ServiceException
	{
		final Customer customer = (Customer) form.getDefaultModelObject();
		taloolService.save(customer);
		SessionUtils.successMessage("Successfully saved customer '", customer.getEmail(), "'");
	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Customer";
	}

	private String confirm;

	public String getConfirm()
	{
		return confirm;
	}

	public void setConfirm(String confirm)
	{
		this.confirm = confirm;
	}

}
