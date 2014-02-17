package com.talool.website.component;

import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.wicketstuff.objectautocomplete.ObjectAutoCompleteField;
import org.wicketstuff.objectautocomplete.ObjectAutoCompleteSelectionChangeListener;

import com.talool.core.Merchant;
import com.talool.website.util.SessionUtils;

public abstract class MerchantSearchPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private String merchantName;
	private UUID merchantId;
	private boolean acSelected = false;
	private ObjectAutoCompleteField<Merchant, UUID> acField;

	public MerchantSearchPanel(String id) {
		super(id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		Form form = new Form("form");
		add(form);
		
		form.add(new AjaxButton("btn")
		{
			private static final long serialVersionUID = 7111919213900349555L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				if (acSelected)
				{
					onSearch(target, merchantName);
					acSelected = false;
					
					// WATCH OUT: We are putting the selected name back in the field, 
					// bc we cleared it on selection to avoid the "selected" state.
					acField.getSearchTextField().getModel().setObject(merchantName);
				}
				else
				{
					// only search if the merchant name has changed
					String val = acField.getSearchTextField().getValue();
					if (!val.equals(merchantName))
					{
						merchantName = val;
						onSearch(target, "*"+merchantName+"*");
					}
				}
				
			}
			
		});

		
		// Auto-complete search field for merchant selection
		MerchantAutoCompleteBuilder acBuilder = new MerchantAutoCompleteBuilder(SessionUtils.getSession().getMerchantAccount().getMerchant());
		ObjectAutoCompleteSelectionChangeListener<UUID> listener = new ObjectAutoCompleteSelectionChangeListener<UUID>()
	    {

			private static final long serialVersionUID = 1L;

			@Override
			public void selectionChanged(AjaxRequestTarget target, IModel<UUID> model) {
				merchantName = acField.getSearchTextField().getValue();
				acSelected = true;
			}
	    	
	    };
	    acBuilder.updateOnSelectionChange(listener);
	    acBuilder.readOnlyRenderer(null);
	    
	    // WATCH OUT: We are using this setting because 
	    // we don't want to see the "selected" state of the component.
	    acBuilder.clearInputOnSelection(); 
	    
		acField = acBuilder.build("ac", new PropertyModel<UUID>(this, "merchantId"));
	    form.add(acField);
	   
	}
	
	public abstract void onSearch(AjaxRequestTarget target, String merchantName);

}
