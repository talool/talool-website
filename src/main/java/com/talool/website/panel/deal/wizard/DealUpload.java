package com.talool.website.panel.deal.wizard;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.ResourceModel;

import com.talool.website.panel.image.upload.FileUploadPanel;

public class DealUpload extends WizardStep {

	private static final long serialVersionUID = 1L;
	
	public DealUpload()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
        
    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		FileUploadPanel fileUpload = new FileUploadPanel("fileUpload");
		add(fileUpload);
	}

}
