package com.talool.website.panel.image.selection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.talool.core.Category;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.core.Tag;
import com.talool.website.models.CategoryListModel;
import com.talool.website.models.CategoryTagListModel;
import com.talool.website.models.MerchantMediaListModel;
import com.talool.website.models.StockMediaListModel;
import com.talool.website.panel.TagPickerPanel;

abstract public class MediaPickerTab extends Panel
{

	private static final long serialVersionUID = -4183138880833396304L;

	private IModel<MerchantMedia> selectedMediaModel;
	private UUID merchantId;
	private MediaType mediaType;
	private Set<Tag> tags;

	private boolean showFilters = false;

	public MediaPickerTab(String id, UUID merchantId, MediaType mediaType, IModel<MerchantMedia> model)
	{
		super(id);

		this.merchantId = merchantId;
		this.mediaType = mediaType;
		selectedMediaModel = model;
		
	}
	
	public MediaPickerTab(String id, UUID merchantId, MediaType mediaType, IModel<MerchantMedia> model, Set<Tag> tags)
	{
		super(id);

		this.merchantId = merchantId;
		this.mediaType = mediaType;
		selectedMediaModel = model;
		this.tags = tags;
		
	}
	
	public MediaPickerTab(String id, UUID merchantId, MediaType mediaType, IModel<MerchantMedia> model, boolean showFilters)
	{
		super(id);

		this.merchantId = merchantId;
		this.mediaType = mediaType;
		selectedMediaModel = model;
		this.showFilters = showFilters;
		
		Category category = (new CategoryListModel()).getObject().get(0);
		CategoryTagListModel tagsModel = new CategoryTagListModel(category);
		Tag tag = tagsModel.getObject().get(0);
		Set<Tag> tags = new HashSet<Tag>();
		tags.add(tag);
		this.tags = tags;

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		ChoiceRenderer<MerchantMedia> cr = new ChoiceRenderer<MerchantMedia>("mediaName", "mediaUrl");
		
		LoadableDetachableModel<List<MerchantMedia>> model;
		if (showFilters || merchantId==null)
		{
			model = new StockMediaListModel(tags);
		}
		else
		{
			MerchantMediaListModel m = new MerchantMediaListModel();
			m.setMediaType(mediaType);
			m.setMerchantId(merchantId);
			model = m;
		}
		final DropDownChoice<MerchantMedia> mediaSelect = new DropDownChoice<MerchantMedia>("availableMedia", selectedMediaModel, model, cr);
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
		
		TagPickerPanel tpp = new TagPickerPanel("tagFilters")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onTagsSelectionComplete(AjaxRequestTarget target, Category category, List<Tag> tl) 
			{
				tags = new HashSet<Tag>();
				tags.addAll(tl);
				mediaSelect.setChoices(new StockMediaListModel(tags));
				target.add(mediaSelect);
				target.appendJavaScript("window.oo.mediaPicker();");
			}
		};
		add(tpp.setVisible(showFilters));
	}

	abstract public void onMediaSelectionComplete(AjaxRequestTarget target, MerchantMedia media);

}
