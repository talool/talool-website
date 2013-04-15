package com.talool.website.panel.merchant.definition;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Image;
import com.talool.website.component.DealImageSelect;
import com.talool.website.models.AvailableDealImagesListModel;

public class ImageSelectPanel extends Panel {

	private static final long serialVersionUID = 8196332332882167487L;
	private DealPreview preview;
	private Image image;

	public ImageSelectPanel(String id, DealPreview preview) {
		super(id);
		this.setMarkupId(id);
		this.preview = preview;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final DealImageSelect images = new DealImageSelect("availableImages", new PropertyModel<Image>(this,
				"image"), new AvailableDealImagesListModel());
		images.setRequired(true);
		images.add(new DealPreviewUpdatingBehavior(preview, DealPreviewUpdatingBehavior.DealComponent.IMAGE, "onChange"));
		add(images);
		
		final ImageSelectPanel self = this;
		
		add(new AjaxLink<Void>("newImageLink")
		{
			private static final long serialVersionUID = -8398298854016035306L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// show the upload panel
				ImageUploadPanel panel = new ImageUploadPanel(self.getMarkupId(), preview);
				self.replaceWith(panel.setOutputMarkupId(true));
				target.add(panel);
			}
		});
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
