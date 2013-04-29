package com.talool.website.panel.deal.definition.template;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.talool.website.panel.deal.DealPreview;

public class MoneyOffDinnerDealTemplatePanel extends DealTemplatePanel {

	private static final long serialVersionUID = 841050421679584729L;
	private String amount, total;
	
	public MoneyOffDinnerDealTemplatePanel(String id, DealPreview preview) {
		super(id, preview);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		TextField<String> amount = new TextField<String>("amount", new PropertyModel<String>(this,"amount"));
		amount.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = 7245087917852774661L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				syncTitle(target);
			}
			
		});
		amount.setRequired(true);
		add(amount);
		
		TextField<String> entree = new TextField<String>("total", new PropertyModel<String>(this,"total"));
		entree.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long serialVersionUID = -8923422105864542517L;

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

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	public String cookUpTitle() {
		StringBuilder sb = new StringBuilder("$");
		sb.append(getAmount()).append(" off a dinner purchase");
		return sb.toString();
	}
	
	public String cookUpSummary() {
		StringBuilder sb = new StringBuilder(cookUpTitle());
		sb.append(" of $").append(getTotal()).append(" or more.");
		return sb.toString();
	}



}
