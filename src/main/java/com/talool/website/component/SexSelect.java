package com.talool.website.component;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.talool.core.Sex;

public class SexSelect extends DropDownChoice<Sex>
{
	private static final long serialVersionUID = 6639221585443609156L;

	private static Sex[] options = new Sex[] { 
		Sex.Unknown,	
		Sex.Female,
		Sex.Male
	};

	private static List<Sex> sexOptions = null;
	private static ChoiceRenderer<Sex> cr =  null;
	static
	{
		sexOptions = Arrays.asList(options);
		cr = new ChoiceRenderer<Sex>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Sex sex) {
				if (sex == Sex.Unknown)
				{
					return "Any";
				}
				else
				{
					return sex.name();
				}
			}
			
		};
	}
	

	public SexSelect(String id)
	{
		super(id, sexOptions, cr);
	}

	public SexSelect(String id, IModel<Sex> model)
	{
		super(id, model, sexOptions, cr);
	}

}
