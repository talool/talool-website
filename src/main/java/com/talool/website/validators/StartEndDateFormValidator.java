package com.talool.website.validators;

import org.apache.wicket.extensions.yui.calendar.DateTimeField;
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

	public StartEndDateFormValidator(final DateTimeField startDate, final DateTimeField endDate)
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

		final DateTimeField startField = (DateTimeField) components[0];
		final DateTimeField endField = (DateTimeField) components[1];

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