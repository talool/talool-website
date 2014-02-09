package com.talool.website.panel.image.selection;

import java.util.List;
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
	private DropDownChoice<MerchantMedia> mediaSelect;

	public MediaPickerTab(String id, UUID merchantId, MediaType mediaType, IModel<MerchantMedia> model)
	{
		super(id);

		mediaListModel = new MerchantMediaListModel();
		mediaListModel.setMerchantId(merchantId);
		mediaListModel.setMediaType(mediaType);

		ChoiceRenderer<MerchantMedia> cr = new ChoiceRenderer<MerchantMedia>("mediaName", "mediaUrl");

		selectedMediaModel = model;
		if (model.getObject()==null)
		{
			List<MerchantMedia> list = mediaListModel.getObject();
			if (!list.isEmpty())
			{
				selectedMediaModel.setObject(list.get(0));
			}
		}

		mediaSelect = new DropDownChoice<MerchantMedia>("availableMedia", selectedMediaModel, mediaListModel, cr);
		mediaSelect.setNullValid(false);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(mediaSelect.setOutputMarkupId(true));
		mediaSelect.add(new AjaxFormComponentUpdatingBehavior("onChange"){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				onMediaSelectionComplete(target, selectedMediaModel.getObject());
			}
			
		});
	}

	public DropDownChoice<MerchantMedia> getMediaSelect()
	{
		return mediaSelect;
	}

	abstract public void onMediaSelectionComplete(AjaxRequestTarget target, MerchantMedia media);

}
