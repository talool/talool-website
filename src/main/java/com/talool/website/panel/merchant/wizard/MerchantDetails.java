package com.talool.website.panel.merchant.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;

public class MerchantDetails extends WizardStep {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDetails.class);
	
	public MerchantDetails()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));

    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		Merchant merchant = (Merchant) getDefaultModelObject();
	}
}

