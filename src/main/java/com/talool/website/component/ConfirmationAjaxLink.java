package com.talool.website.component;

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;

/**
 * 
 * @author clintz
 * 
 * @param <T>
 */
public abstract class ConfirmationAjaxLink<T> extends AjaxLink<T>
{
	private static final long serialVersionUID = 1L;
	private final String text;

	public ConfirmationAjaxLink(String id, String text)
	{
		super(id);
		this.text = text;
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
	{
		super.updateAjaxAttributes(attributes);

		AjaxCallListener ajaxCallListener = new AjaxCallListener();

		ajaxCallListener.onPrecondition("return confirm('" + text + "');");

		attributes.getAjaxCallListeners().add(ajaxCallListener);
	}
}