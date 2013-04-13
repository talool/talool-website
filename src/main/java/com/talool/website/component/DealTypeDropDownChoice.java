package com.talool.website.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.core.DealType;

/**
 * 
 * @author clintz
 * 
 */
public class DealTypeDropDownChoice extends DropDownChoice<DealType>
{
	private static final long serialVersionUID = 5523330470524619225L;

	private static final List<DealType> choices = Arrays.asList(DealType.values());

	public DealTypeDropDownChoice(String id, IModel<DealType> model)
	{
		super(id, model, choices);
	}

	public DealTypeDropDownChoice(String id)
	{
		super(id, choices);
	}

}
