package com.talool.website.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.core.MerchantIdentity;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantIdentitySelect extends DropDownChoice<MerchantIdentity>
{
	private static final long serialVersionUID = 1444703616744009829L;
	private static final ChoiceRenderer<MerchantIdentity> choiceRenderer = new ChoiceRenderer<MerchantIdentity>(
			"name", "id");

	public MerchantIdentitySelect(String id, IModel<MerchantIdentity> model,
			IModel<? extends List<? extends MerchantIdentity>> choices)
	{
		super(id, model, choices, choiceRenderer);
	}

}
