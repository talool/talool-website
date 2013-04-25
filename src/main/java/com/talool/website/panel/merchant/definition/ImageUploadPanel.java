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

import com.talool.core.Image;
import com.talool.website.models.ModelUtil;

public class ImageUploadPanel extends Panel
{

	private static final long serialVersionUID = 2266250361647577936L;
	private static final Logger LOG = LoggerFactory.getLogger(ImageUploadPanel.class);
	private List<FileUpload> fileUploads;
	private DealPreview preview;
	private PropertyModel<Image> model;

	public ImageUploadPanel(String id, PropertyModel<Image> model, DealPreview preview)
	{
		super(id);
		setMarkupId(id);
		this.preview = preview;
		this.model = model;
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
				ImageSelectPanel panel = new ImageSelectPanel(self.getMarkupId(), model, preview);
				self.replaceWith(panel.setOutputMarkupId(true));
				target.add(panel);
			}
		});
	}

	public String getUpload()
	{
		String url = null;
		if (CollectionUtils.isNotEmpty(fileUploads))
		{
			try
			{
				url = ModelUtil.saveUploadImage(fileUploads.get(0));
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
