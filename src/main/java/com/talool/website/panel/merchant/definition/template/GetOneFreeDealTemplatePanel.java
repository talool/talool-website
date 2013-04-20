package com.talool.website.panel.merchant.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.website.panel.merchant.definition.DealPreview;

public class GetOneFreeDealTemplatePanel extends DealTemplatePanel {

	private static final long serialVersionUID = 3422512443138120125L;
	private String entree;
	
	public GetOneFreeDealTemplatePanel(String id, DealPreview preview, CompoundPropertyModel<Deal> model) {
		super(id, preview, model);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		TextField<String> entree = new TextField<String>("entree", new PropertyModel<String>(this,"entree"));
		entree.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 309096430747719688L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				syncTitle(target);
				syncSummary(target);
			}
			
		});
		entree.setRequired(true);
		add(entree);

	}

	public String getEntree() {
		return entree;
	}

	public void setEntree(String entree) {
		this.entree = entree;
	}
	
	public String cookUpTitle() {
		return "Get One Free";
	}
	
	public String cookUpSummary() {
		StringBuilder sb = new StringBuilder("Buy one ");
		sb.append(getEntree()).append(", get one one of equal or lesser value free.");
		return sb.toString();
	}



}
