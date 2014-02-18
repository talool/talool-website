package com.talool.website.component;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public abstract class CustomerSearchPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private String customerEmail;
	private TextField<String> emailField;

	public CustomerSearchPanel(String id) {
		super(id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		Form form = new Form("form");
		add(form);
		
		emailField = new TextField<String>("searchInput", new PropertyModel(this,"customerEmail"));
		form.add(emailField.setOutputMarkupId(true));
		
		form.add(new AjaxButton("btn")
		{
			private static final long serialVersionUID = 7111919213900349555L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (StringUtils.isEmpty(customerEmail))
				{
					onSearch(target, "");
				}
				else
				{
					onSearch(target, "*"+customerEmail+"*");
				}
				
			}
			
		});
	   
	}
	
	public String getCustomerEmail() {
		return customerEmail;
	}

	public abstract void onSearch(AjaxRequestTarget target, String customerEmail);

}
