package com.talool.website.panel.merchant.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.website.panel.merchant.definition.DealPreview;

public class PercentOffDealTemplatePanel extends DealTemplatePanel {

	private static final long serialVersionUID = 8249845899184103868L;
	private String amount, entree;
	
	public PercentOffDealTemplatePanel(String id, DealPreview preview, CompoundPropertyModel<Deal> model) {
		super(id, preview, model);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		TextField<String> amount = new TextField<String>("amount", new PropertyModel<String>(this,"amount"));
		amount.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 3430295556827146206L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				syncTitle(target);
			}
			
		});
		amount.setRequired(true);
		add(amount);
		
		TextField<String> entree = new TextField<String>("entree", new PropertyModel<String>(this,"entree"));
		entree.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 9212424796499074393L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				syncSummary(target);
			}
			
		});
		entree.setRequired(true);
		add(entree);

	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEntree() {
		return entree;
	}

	public void setEntree(String entree) {
		this.entree = entree;
	}
	
	public String cookUpTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append(getAmount()).append("% Off");
		return sb.toString();
	}
	
	public String cookUpSummary() {
		StringBuilder sb = new StringBuilder(cookUpTitle());
		sb.append(" of the regular price of ").append(getEntree()).append(".");
		return sb.toString();
	}



}
