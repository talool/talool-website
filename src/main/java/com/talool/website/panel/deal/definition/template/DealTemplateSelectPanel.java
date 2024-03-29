package com.talool.website.panel.deal.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.website.component.DealTemplateSelect;
import com.talool.website.models.DealTemplateListModel;
import com.talool.website.panel.deal.DealPreview;

public class DealTemplateSelectPanel extends Panel {

	private static final long serialVersionUID = 8196332332882167487L;
	private DealPreview preview;
	private String template = "Other";
	private final String dealTemplateMarkupId = "dealTemplate";

	public DealTemplateSelectPanel(String id, DealPreview preview) {
		super(id);
		this.setMarkupId(id);
		this.preview = preview;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final DealTemplateSelect templates = new DealTemplateSelect("templateSelect",new PropertyModel<String>(this,"template"), new DealTemplateListModel());
		add(templates);
		
		final Deal deal = (Deal) getDefaultModelObject();
		
		// This is the starting panel
		// TODO this should be driven off of a property on the deal or the user
		DealTemplatePanel dTemplate = new OtherDealTemplatePanel(dealTemplateMarkupId, preview, deal.getTitle(), deal.getSummary());
		add(dTemplate.setOutputMarkupId(true));
		dTemplate.setDefaultModel(getDefaultModel());

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
					case 0: panel1 = new MoneyOffDealTemplatePanel(dealTemplateMarkupId, preview);
						break;
					case 1: panel1 = new GetOneFreeDealTemplatePanel(dealTemplateMarkupId, preview);
						break;
					case 2: panel1 = new PercentOffDealTemplatePanel(dealTemplateMarkupId, preview);
						break;
					case 3: panel1 = new MoneyOffLunchDealTemplatePanel(dealTemplateMarkupId, preview);
						break;
					case 4: panel1 = new MoneyOffDinnerDealTemplatePanel(dealTemplateMarkupId, preview);
						break;
					default: panel1 = new OtherDealTemplatePanel(dealTemplateMarkupId, preview, deal.getTitle(), deal.getSummary());
				}
				
				// swap the panels
				panel1.setDefaultModel(getDefaultModel());
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
