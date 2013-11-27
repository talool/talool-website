package com.talool.website.component;

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;

/**
 * Shows an indicating spinner next to AjaxLink
 * 
 * @author clintz
 * 
 * @param <T>
 */
public abstract class ConfirmationIndicatingAjaxLink<T> extends IndicatingAjaxLink<T>
{
	private static final long serialVersionUID = 1L;
	private final String text;

	public ConfirmationIndicatingAjaxLink(String id, String text)
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