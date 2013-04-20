package com.talool.website.panel.merchant.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.website.component.DealTemplateSelect;
import com.talool.website.models.DealTemplateListModel;
import com.talool.website.panel.merchant.definition.DealPreview;

public class DealTemplateSelectPanel extends Panel {

	private static final long serialVersionUID = 8196332332882167487L;
	private DealPreview preview;
	private CompoundPropertyModel<Deal> model;
	private String template = "Other";
	private final String dealTemplateMarkupId = "dealTemplate";

	public DealTemplateSelectPanel(String id, CompoundPropertyModel<Deal> model, DealPreview preview) {
		super(id);
		this.setMarkupId(id);
		this.preview = preview;
		this.model = model;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final DealTemplateSelect templates = new DealTemplateSelect("templateSelect",new PropertyModel<String>(this,"template"), new DealTemplateListModel());
		add(templates);
		
		// This is the starting panel
		// TODO this should be driven off of a property on the deal or the user
		DealTemplatePanel dTemplate = new OtherDealTemplatePanel(dealTemplateMarkupId, preview, model);
		add(dTemplate.setOutputMarkupId(true));

		final DealTemplateSelectPanel self = this;
		
		templates.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// get the current panel
				DealTemplatePanel panel0 = (DealTemplatePanel)self.get(dealTemplateMarkupId);
				
				// pick the correct new panel based on the select box
				DealTemplatePanel panel1;
				Integer selectedValue = new Integer(templates.getValue());
				switch (selectedValue) {
					case 0: panel1 = new MoneyOffDealTemplatePanel(dealTemplateMarkupId, preview, model);
						break;
					case 1: panel1 = new GetOneFreeDealTemplatePanel(dealTemplateMarkupId, preview, model);
						break;
					case 2: panel1 = new PercentOffDealTemplatePanel(dealTemplateMarkupId, preview, model);
						break;
					case 3: panel1 = new MoneyOffLunchDealTemplatePanel(dealTemplateMarkupId, preview, model);
						break;
					case 4: panel1 = new MoneyOffDinnerDealTemplatePanel(dealTemplateMarkupId, preview, model);
						break;
					default: panel1 = new OtherDealTemplatePanel(dealTemplateMarkupId, preview, model);
				}
				
				// swap the panels
				panel0.replaceWith(panel1.setOutputMarkupId(true));
				target.add(panel1);
			}
		});
	}
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
