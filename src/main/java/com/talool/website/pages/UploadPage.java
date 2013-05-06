package com.talool.website.pages;

import org.apache.wicket.markup.html.WebPage;

import com.talool.website.panel.image.upload.FileUploadPanel;

public class UploadPage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		FileUploadPanel fileUpload = new FileUploadPanel("fileUpload");
        add(fileUpload);
	}

}
