package com.talool.website.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;

abstract public class RangeSlider extends Panel {

	private static final long serialVersionUID = -7445224667801029952L;
	private Integer min;
	private Integer max;
	private Integer startLow;
	private Integer startHigh;
	private TextField<String> amountFld;
	private WebMarkupContainer slider;

	public RangeSlider(String id, IModel<Integer[]> model) {
		super(id, model);
		setOutputMarkupId(true);
		
		Integer[] range = model.getObject();
		if (range.length >= 2)
		{
			min = range[0];
			max = range[1];
			if (range.length >= 4)
			{
				startLow = range[2];
				startHigh = range[3];
			}
			else
			{
				startLow = 0;
				startHigh = 0;
			}
		}
		else
		{
			min = 0;
			startLow = 0;
			max = 100;
			startHigh = 0;
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		
		slider = new WebMarkupContainer("slider");
		container.add(slider.setOutputMarkupId(true));
		amountFld = new TextField<String>("amount", Model.of(""));
		container.add(amountFld.setOutputMarkupId(true));
		
		// add data attributes for slider initialization (core.js initSliders)
		container.add(new AttributeModifier("data-amt-fld-id", amountFld.getMarkupId()));
		container.add(new AttributeModifier("data-slider-id", slider.getMarkupId()));
		container.add(new AttributeModifier("data-min", min));
		container.add(new AttributeModifier("data-max", max));
		container.add(new AttributeModifier("data-low", startLow));
		container.add(new AttributeModifier("data-high", startHigh));
		
		// create a callback so the slider can tell us what changed
		final AbstractDefaultAjaxBehavior behave = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			protected void respond(final AjaxRequestTarget target) {
				IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
				startLow = params.getParameterValue("min").toInteger();
				startHigh = params.getParameterValue("max").toInteger();
		    	onChangeComplete(target, new Integer[] {startLow,startHigh});
		    }
		};
		add(behave);
		container.add(new AttributeModifier("data-callback", behave.getCallbackUrl().toString()));
	}
	
	abstract public void onChangeComplete(AjaxRequestTarget target, Integer[] range);

}
