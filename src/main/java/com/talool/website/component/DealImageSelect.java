package com.talool.website.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.core.Image;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealImageSelect extends DropDownChoice<Image>
{
	private static final long serialVersionUID = 1444703616744009829L;
	private static final ChoiceRenderer<Image> choiceRenderer = new ChoiceRenderer<Image>(
			"label", "url");

	public DealImageSelect(String id, IModel<Image> model,
			IModel<? extends List<? extends Image>> choices)
	{
		super(id, model, choices, choiceRenderer);
	}

}
