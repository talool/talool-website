package com.talool.website.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * 
 * @author chris.lintz
 * 
 */
public class EmailValidator implements IValidator<String>
{

	private static final long serialVersionUID = 4899646225054591622L;
	private static EmailValidator instance = new EmailValidator();
	private static FormValidator validator = FormValidator.getInstance();
	private static String EMAIL_INVALID = "order.generic.email.error";

	public static EmailValidator getInstance()
	{
		return instance;
	}

	@Override
	public void validate(IValidatable<String> validatable)
	{

		if (!validator.isEmail(validatable.getValue()))
		{
			error(validatable, "invalid.email");
		}
	}

	private void error(IValidatable<String> validatable, String errorKey)
	{
		ValidationError error = new ValidationError();
		error.addKey(getClass().getSimpleName() + "." + errorKey);
		validatable.error(error);
	}
}