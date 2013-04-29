package com.talool.website.behaviors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

/**
 * A nice way to force the component to set the model when the field changes
 * 
 * @author clintz
 * 
 */
public class OnChangeAjaxFormBehavior extends AjaxFormComponentUpdatingBehavior
{
	public OnChangeAjaxFormBehavior()
	{
		super("onchange");
	}

	private static final long serialVersionUID = -6571165071841384218L;

	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
		// do nothing purposely
		System.out.println("Here");

	}
}