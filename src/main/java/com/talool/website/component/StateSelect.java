package com.talool.website.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * wicket StateDropDown component (includes Canada)
 * 
 * @author chris.lintz
 * @todo Figure out how to create a select label so we can put this label in
 *       between states and canada.
 * 
 */
@SuppressWarnings("unchecked")
public class StateSelect extends DropDownChoice<StateOption>
{
	private static final long serialVersionUID = 6639221585443609156L;
	private static Map<String, StateOption> stateMap = new HashMap<String, StateOption>();
	private static StateOption OTHER = new StateOption("OT","Other");

	private static StateOption[] options = new StateOption[] { 
			new StateOption("AL", "Alabama"),
			new StateOption("AK", "Alaska"), 
			new StateOption("AZ", "Arizona"), 
			new StateOption("AR", "Arkansas"),
			new StateOption("CA", "California"), 
			new StateOption("CO", "Colorado"),
			new StateOption("CT", "Connecticut"),

			new StateOption("DE", "Delaware"), 
			new StateOption("DC", "District of Columbia"),
			new StateOption("FL", "Florida"),
			new StateOption("GA", "Georgia"), 
			new StateOption("HI", "Hawaii"), 
			new StateOption("ID", "Idaho"),

			new StateOption("IL", "Illinois"), 
			new StateOption("IN", "Indiana"), 
			new StateOption("IA", "Iowa"), 
			new StateOption("KS", "Kansas"),
			new StateOption("KY", "Kentucky"), 
			new StateOption("LA", "Louisiana"),
			new StateOption("ME", "Maine"), 
			new StateOption("MD", "Maryland"), 
			new StateOption("MA", "Massachusetts"),

			new StateOption("MI", "Michigan"), 
			new StateOption("MN", "Minnesota"),
			new StateOption("MS", "Mississippi"), 
			new StateOption("MO", "Missouri"),
			new StateOption("MT", "Montana"), 
			new StateOption("NE", "Nebraska"),
			new StateOption("NV", "Nevada"), 
			new StateOption("NH", "New Hampshire"),

			new StateOption("NJ", "New Jersey"), 
			new StateOption("NM", "New Mexico"),
			new StateOption("NY", "New York"), 
			new StateOption("NC", "North Carolina"),
			new StateOption("ND", "North Dakota"), 
			new StateOption("OH", "Ohio"), 
			new StateOption("OK", "Oklahoma"),

			new StateOption("OR", "Oregon"), 
			new StateOption("PA", "Pennsylvania"), 
			new StateOption("RI", "Rhode Island"), 
			new StateOption("SC", "South Carolina"),
			new StateOption("SD", "South Dakota"), 
			new StateOption("TN", "Tennessee"),

			new StateOption("TX", "Texas"), 
			new StateOption("UT", "Utah"),
			new StateOption("VT", "Vermont"), 
			new StateOption("VA", "Virginia"), 
			new StateOption("WA", "Washington"),
			new StateOption("WV", "West Virginia"), 
			new StateOption("WI", "Wisconsin"),

			new StateOption("WY", "Wyoming")
	};

	static
	{
		for (final StateOption so : options)
		{
			stateMap.put(so.getCode(), so);
		}
	}

	private static List<StateOption> stateOptions = null;

	private static ChoiceRenderer cr = null;
	static
	{
		stateOptions = Arrays.asList(options);
		cr = new ChoiceRenderer("state", "code");
	}

	public static StateOption getStateOptionByCode(final String code)
	{
		StateOption so = stateMap.get(code);
		return so == null ? OTHER : so;
	}

	public StateSelect(String id)
	{
		super(id, stateOptions, cr);
	}

	public StateSelect(String id, IModel<StateOption> model)
	{
		super(id, model, stateOptions, cr);
	}

}
