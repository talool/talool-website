package com.talool.website.panel.deal.definition;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;
import com.talool.core.Image;
import com.talool.domain.ImageImpl;
import com.talool.website.panel.BasePanel;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.definition.template.DealTemplateSelectPanel;

public class DealDetailsPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	private Image image;

	// TODO add the various constructors?
	public DealDetailsPanel(String id) {
		super(id);
		
		image = new ImageImpl("Test Image 1",
				"http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test.png");
		
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		Deal deal = (Deal) getDefaultModelObject();
		
		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		add(dealPreview);
		
		add(new DealTemplateSelectPanel("templateSelectPanel", dealPreview));
		
		final WebMarkupContainer imageSelect = new WebMarkupContainer("imageSelectContainer");
		imageSelect.setOutputMarkupId(true);
		add(imageSelect);
		
		ImageSelectPanel images = new ImageSelectPanel("imageSelectPanel", new PropertyModel<Image>(
				this, "image"), dealPreview);
		images.setOutputMarkupId(true);
		imageSelect.add(images);
		
	}

}
