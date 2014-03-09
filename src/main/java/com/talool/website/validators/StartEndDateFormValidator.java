package com.talool.website.validators;

import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * A form validator that compares two dates. If the enddate is before the
 * startdate an error occurs
 * 
 * @author clintz
 * 
 */
public class StartEndDateFormValidator extends AbstractFormValidator
{

	private final FormComponent<?>[] components;

	private static final long serialVersionUID = -3481049618080999489L;

	public StartEndDateFormValidator(final DateTextField startDate, final DateTextField endDate)
	{
		components = new FormComponent<?>[] { startDate, endDate };
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents()
	{
		return components;
	}

	@Override
	public void validate(Form<?> form)
	{

		final DateTextField startField = (DateTextField) components[0];
		final DateTextField endField = (DateTextField) components[1];

		if (endField.getConvertedInput() == null || startField.getConvertedInput() == null)
		{
			return;
		}

		if (endField.getConvertedInput().getTime() < startField.getConvertedInput().getTime())
		{
			error(components[1], "enddate.is.before.startdate");
		}

	}

}