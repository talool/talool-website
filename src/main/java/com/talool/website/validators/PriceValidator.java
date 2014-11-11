package com.talool.website.validators;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * 
 * @author dmccuen
 * 
 */
public class PriceValidator implements IValidator<Float>
{

	private static final long serialVersionUID = 4899646225054591622L;
	private static PriceValidator instance = new PriceValidator();
	private static FormValidator validator = FormValidator.getInstance();
	private static final int MIN_PRICE = 0;
	private static final int MAX_PRICE = 1000;

	public static PriceValidator getInstance()
	{
		return instance;
	}

	@Override
	public void validate(IValidatable<Float> validatable)
	{
		Float price = validatable.getValue();
		
		if (!validator.isInRange(price.intValue(), MIN_PRICE, MAX_PRICE))
		{
			error(validatable, "invalid.price");
		}
	}

	private void error(IValidatable<Float> validatable, String errorKey)
	{
		ValidationError error = new ValidationError();
		error.addKey(getClass().getSimpleName() + "." + errorKey);
		
		Map<String,Object> vars = new HashMap<String,Object>();
		vars.put("minimum", "$"+MIN_PRICE);
		vars.put("maximum", "$"+MAX_PRICE);
		error.setVariables(vars);
		
		validatable.error(error);
	}
}