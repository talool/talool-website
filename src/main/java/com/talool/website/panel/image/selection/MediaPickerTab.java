package com.talool.website.panel.image.selection;

import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.website.models.MerchantMediaListModel;

abstract public class MediaPickerTab extends Panel
{

	private static final long serialVersionUID = -4183138880833396304L;

	private MerchantMediaListModel mediaListModel;
	private IModel<MerchantMedia> selectedMediaModel;

	public MediaPickerTab(String id, UUID merchantId, MediaType mediaType, IModel<MerchantMedia> model)
	{
		super(id);

		mediaListModel = new MerchantMediaListModel();
		mediaListModel.setMerchantId(merchantId);
		mediaListModel.setMediaType(mediaType);
		
		selectedMediaModel = model;
		
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		ChoiceRenderer<MerchantMedia> cr = new ChoiceRenderer<MerchantMedia>("mediaName", "mediaUrl");
		
		DropDownChoice<MerchantMedia> mediaSelect = new DropDownChoice<MerchantMedia>("availableMedia", selectedMediaModel, mediaListModel, cr);
		mediaSelect.setNullValid(true);
		mediaSelect.setOutputMarkupId(true);
		add(mediaSelect);
		
		mediaSelect.add(new AjaxFormComponentUpdatingBehavior("onChange"){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				onMediaSelectionComplete(target, selectedMediaModel.getObject());
			}
			
		});
	}

	abstract public void onMediaSelectionComplete(AjaxRequestTarget target, MerchantMedia media);

}
