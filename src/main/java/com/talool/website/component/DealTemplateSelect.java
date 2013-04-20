package com.talool.website.component;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.website.models.DealTemplateListModel;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealTemplateSelect extends DropDownChoice<String>
{


	private static final long serialVersionUID = -7969385980901889696L;
	//private static final ChoiceRenderer<Propery> choiceRenderer = new ChoiceRenderer<Propery>("name", "value");

	public DealTemplateSelect(String id, IModel<String> model, DealTemplateListModel choices)
	{
		//super(id, model, choices, choiceRenderer);
		super(id, model, choices);
	}

}
