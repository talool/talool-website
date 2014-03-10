package com.talool.website.component;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public abstract class MerchantSearchPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private String merchantName;
	private UUID merchantId;
	private TextField<String> nameField;

	public MerchantSearchPanel(String id) {
		super(id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		Form form = new Form("form");
		add(form);
		
		nameField = new TextField<String>("searchInput", new PropertyModel(this,"merchantName"));
		form.add(nameField.setOutputMarkupId(true));
		
		form.add(new AjaxButton("btn")
		{
			private static final long serialVersionUID = 7111919213900349555L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) 
			{
				
				if (StringUtils.isEmpty(merchantName))
				{
					onSearch(target, "");
				}
				else
				{
					onSearch(target, "*"+merchantName+"*");
				}
				
			}
			
		});

	   
	}
	
	public abstract void onSearch(AjaxRequestTarget target, String merchantName);

}
