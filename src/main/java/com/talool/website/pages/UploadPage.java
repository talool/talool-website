package com.talool.website.pages;

import java.util.UUID;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.talool.core.MediaType;
import com.talool.website.panel.image.upload.FileUploadPanel;

public class UploadPage extends WebPage {

	private static final long serialVersionUID = 1L;
	private final UUID merchantId;
    private final MediaType mediaType;
    
	public UploadPage(PageParameters params)
	{
		super(params);
		
		StringValue id = params.get("id");
		if (id==null)
		{
			merchantId = null;
		}
		else
		{
			merchantId = UUID.fromString(id.toString());
		}
		
		StringValue type = params.get("type");
		if (type==null)
		{
			mediaType = null;
		}
		else
		{
			mediaType = MediaType.valueOf(type.toString());
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		FileUploadPanel fileUpload = new FileUploadPanel("fileUpload", merchantId, mediaType);
        add(fileUpload);
	}

}
