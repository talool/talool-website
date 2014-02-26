package com.talool.website.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

abstract public class ToggleButton extends Panel {

	private static final long serialVersionUID = 1L;
	
	private String label;
	private ToggleLabelType labelType;
	
	public enum ToggleLabelType {YES_NO, ON_OFF};

	public ToggleButton(String id, IModel<Boolean> model, ToggleLabelType lt) {
		super(id, model);
		labelType = lt;
		setDefaultModel(model);
		setLabel();
		setOutputMarkupId(true);
	}
	
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		
		final AjaxLink<String> link = new AjaxLink<String>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				MarkupContainer toggle = getParent();
				boolean b = (Boolean)toggle.getDefaultModelObject();
				toggle.setDefaultModelObject(!b);
				target.add(toggle);
				
				this.add(new AttributeModifier("class", getCssClassName()));
				
				onToggle(target);
			}
		};
		add(link.setOutputMarkupId(true));
		link.add(new AttributeModifier("class", getCssClassName()));
		link.add(new Label("label", new PropertyModel<String>(this, "label")).setOutputMarkupId(true));
	}

	@Override
	protected void onModelChanged() {
		super.onModelChanged();
		setLabel();
	}

	private void setLabel()
	{
		boolean b = (Boolean)getDefaultModelObject();
		if (labelType == ToggleLabelType.YES_NO)
		{
			label = b ? "yes":"no";
		}
		else
		{
			label = b ? "on":"off";
		}
	}
	
	private String getCssClassName()
	{
		boolean b = (Boolean)getDefaultModelObject();
		return b ? "on":"off";
	}

	public abstract void onToggle(AjaxRequestTarget target);

}
