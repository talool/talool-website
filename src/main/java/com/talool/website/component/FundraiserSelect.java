package com.talool.website.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.stats.MerchantSummary;

/**
 * 
 * @author dmccuen
 * 
 */
public class FundraiserSelect extends DropDownChoice<MerchantSummary>
{
	private static final long serialVersionUID = 1444703616744009829L;
	private static final ChoiceRenderer<MerchantSummary> choiceRenderer = new ChoiceRenderer<MerchantSummary>(
			"name", "name");

	public FundraiserSelect(String id, IModel<MerchantSummary> model,
			IModel<? extends List<? extends MerchantSummary>> choices)
	{
		super(id, model, choices, choiceRenderer);
	}

}
