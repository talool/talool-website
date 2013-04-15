package com.talool.website.panel.merchant.definition;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.Config;
import com.talool.website.models.ModelUtil;

public class ImageUploadPanel extends Panel {

	private static final long serialVersionUID = 2266250361647577936L;
	private static final Logger LOG = LoggerFactory.getLogger(ImageUploadPanel.class);
	private FileUploadField fileUploadField;
	private List<FileUpload> fileUploads;
	private DealPreview preview;

	public ImageUploadPanel(String id, DealPreview preview) {
		super(id);
		setMarkupId(id);
		this.preview = preview;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		FileUploadField uploader = new FileUploadField("fileUploads", 
				new PropertyModel<List<FileUpload>>(this, "fileUploads"));
		add(uploader);
		
		final ImageUploadPanel self = this;
		
		add(new AjaxLink<Void>("selectImageLink")
		{
			private static final long serialVersionUID = -4121628859507413650L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// show the select panel
				ImageSelectPanel panel = new ImageSelectPanel(self.getMarkupId(), preview);
				self.replaceWith(panel.setOutputMarkupId(true));
				target.add(panel);
			}
		});
	}
	
	// DO I STILL NEED THIS?
	public String getUpload() {
		String url = null;
		if (CollectionUtils.isNotEmpty(fileUploads))
		{
			try
			{
				final String imageName = ModelUtil.saveUploadImage(fileUploads.get(0));
				if (imageName != null)
				{
					url = (Config.get().getStaticLogoBaseUrl() + imageName);
				}
			}
			catch (Exception e)
			{
				LOG.error("Problem with logo upload:" + e.getLocalizedMessage(), e);
				getSession().error("There was a problem with logo upload");
			}
		}
		return url;
	}

}
