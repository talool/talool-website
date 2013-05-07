package com.talool.website.component;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.core.MerchantMedia;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealImageSelect extends DropDownChoice<MerchantMedia>
{
	private static final long serialVersionUID = 1444703616744009829L;
	private static final ChoiceRenderer<MerchantMedia> choiceRenderer = new ChoiceRenderer<MerchantMedia>(
			"mediaName", "mediaUrl");

	public DealImageSelect(String id, IModel<MerchantMedia> model,
			IModel<? extends List<? extends MerchantMedia>> choices)
	{
		super(id, model, choices, choiceRenderer);
	}

}
