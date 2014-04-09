package com.talool.website.panel.analytics;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class CubismPanel extends Panel {

	private static final long serialVersionUID = -2630791674862025637L;

	private String chartName;
	private List<CubismHorizon> horizons;
	private CubismStep step;
	
	private static final int size = 1000;
	
	// Define the unit of measure for a "step"
	// based on a chart width of 1000px, 
	// (60*60*1000)/500 = 3600, (3.6 sec)
	// round up to 4000 so graphite doesn't barf
	private static final double oneHour = 4000; 
	
	// steps must be great than our lowest retention threshold (10s or 10000)
	public static CubismStep[] SUPPORTED_STEPS = { 
		new CubismStep(oneHour*4, "4 Hours"),
		new CubismStep(oneHour*24, "1 Day"),
		new CubismStep(oneHour*24*7, "1 Week"),
		new CubismStep(oneHour*24*7*4, "4 Weeks")
		};

	private static final StepChoiceRenderer renderer = new StepChoiceRenderer();
	private final static List<CubismStep> choices = Arrays.asList(SUPPORTED_STEPS);
	
	public CubismPanel(String id, String chartName, List<CubismHorizon> metrics)
	{
		super(id);
		this.chartName = chartName;
		this.horizons = metrics;
		step = choices.get(0);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final PropertyModel<CubismStep> model = new PropertyModel<CubismStep>(this,"step");
		
		add(new Label("chartName", chartName));
		
		final WebMarkupContainer chart = new WebMarkupContainer("chartCanvas");
		add(chart.setOutputMarkupId(true));
		chart.add(new CubismBehavior(horizons, model, size));

		DropDownChoice<CubismStep> stepPicker = new DropDownChoice<CubismStep>("chartStep", model, choices, renderer);
		add(stepPicker.setOutputMarkupId(true));
		stepPicker.add(new AjaxFormComponentUpdatingBehavior("onChange"){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) 
			{	
				CubismBehavior b = new CubismBehavior(horizons, model, size);
				target.appendJavaScript(b.getChartConfig(chart));
			}
			
		});
		
		
		
	}
	
	private static class StepChoiceRenderer implements IChoiceRenderer<CubismStep>
	{
		private static final long serialVersionUID = 6143588400264652991L;

		@Override
		public Object getDisplayValue(final CubismStep cs)
		{
			return cs.label;
		}

		@Override
		public String getIdValue(CubismStep cs, int index) {
			StringBuilder sb = new StringBuilder();
			return sb.append(cs.step).append(index).toString();
		}
	}
	
	
}
