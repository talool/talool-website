package com.talool.website.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.core.DealOffer;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealOfferSelect extends DropDownChoice<DealOffer>
{
	private static final long serialVersionUID = 1444703616744009829L;
	private static final ChoiceRenderer<DealOffer> choiceRenderer = new ChoiceRenderer<DealOffer>(
			"title", "id");

	public DealOfferSelect(String id, IModel<DealOffer> model,
			IModel<? extends List<? extends DealOffer>> choices)
	{
		super(id, model, choices, choiceRenderer);
	}

}
