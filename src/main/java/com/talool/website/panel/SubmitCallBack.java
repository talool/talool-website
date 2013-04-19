package com.talool.website.panel;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * 
 * @author clintz
 * 
 */
public interface SubmitCallBack extends Serializable
{
	public abstract void submitSuccess(AjaxRequestTarget target);

	public abstract void submitFailure(AjaxRequestTarget target);
	
	public abstract void submitCancel(AjaxRequestTarget target);

}
