package com.talool.website.component;

import java.util.Date;

import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.model.IModel;

/**
 * 
 * @author clintz
 * 
 */
public class DateTimeFieldExtended extends DateTimeField
{
	private static final long serialVersionUID = -9188851208543033502L;

	public DateTimeFieldExtended(String id, IModel<Date> model)
	{
		super(id, model);
	}

	public DateTimeFieldExtended(String id)
	{
		super(id);

	}

	@Override
	protected boolean use12HourFormat()
	{
		return true;
	}

}
