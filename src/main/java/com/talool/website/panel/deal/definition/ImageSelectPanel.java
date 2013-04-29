package com.talool.website.panel.deal.definition;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Image;
import com.talool.website.component.DealImageSelect;
import com.talool.website.models.AvailableDealImagesListModel;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;

public class ImageSelectPanel extends Panel {

	private static final long serialVersionUID = 8196332332882167487L;
	private DealPreview preview;
	private PropertyModel<Image> model;

	public ImageSelectPanel(String id, PropertyModel<Image> model, DealPreview preview) {
		super(id);
		this.setMarkupId(id);
		this.preview = preview;
		this.model = model;
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final DealImageSelect images = new DealImageSelect("availableImages", model, new AvailableDealImagesListModel());
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
				ImageUploadPanel panel = new ImageUploadPanel(self.getMarkupId(), model, preview);
				self.replaceWith(panel.setOutputMarkupId(true));
				target.add(panel);
			}
		});
	}

}
