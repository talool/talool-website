package com.talool.website.panel.image.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Category;
import com.talool.core.MerchantMedia;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.website.models.StockMediaListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BasePanel;
import com.talool.website.panel.TagPickerPanel;

public class MediaTaggerPanel extends BasePanel
{
	private static final Logger LOG = LoggerFactory.getLogger(MediaTaggerPanel.class);
	private static final long serialVersionUID = -4183138880833396304L;

	private Set<Tag> tags;
	private Set<MerchantMedia> media;
	private IModel<Set<MerchantMedia>> selectedMedia;
	
	private ListMultipleChoice<MerchantMedia> mediaSelect;
	private TagPickerPanel tagPicker;
	
	private AjaxLink<Void> applyTags;
	
	public MediaTaggerPanel(String id)
	{
		super(id);
		tags = new HashSet<Tag>();
		media = new HashSet<MerchantMedia>();
		selectedMedia = new PropertyModel<Set<MerchantMedia>>(this, "media");
	}
	

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final BasePage page = (BasePage) getPage();
		
		applyTags = new AjaxLink<Void>("applyTags")
		{
			private static final long serialVersionUID = 268692101349122303L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();

				List<Tag> tl = new ArrayList<Tag>();
				tl.addAll(tags);
				
				try
				{
					for (MerchantMedia m : media)
					{
						taloolService.reattach(m);
						m.addTags(tl);
						taloolService.saveMerchantMedia(m);
					}
					info("Media Tagged.");
				}
				catch (ServiceException se)
				{
					LOG.error("failed to tag media", se);
					error("Failed to tage media.");
				}
				target.add(page.feedback);
				tags = new HashSet<Tag>();
				media = new HashSet<MerchantMedia>();
				
				mediaSelect.setChoices(new StockMediaListModel(tags));
				target.add(mediaSelect);
				target.appendJavaScript("window.oo.mediaPicker();");
				
				target.add(tagPicker);
				
				enableTagging(target);
			}
		};
		applyTags.setOutputMarkupId(true);
		add(applyTags.setEnabled(false));

		ChoiceRenderer<MerchantMedia> cr = new ChoiceRenderer<MerchantMedia>("mediaName", "mediaUrl");		
		LoadableDetachableModel<List<MerchantMedia>> availableMedia = new StockMediaListModel(tags);
		
		mediaSelect =  new ListMultipleChoice<MerchantMedia>("availableMedia", selectedMedia, availableMedia, cr);
		mediaSelect.setOutputMarkupId(true);
		add(mediaSelect.setRequired(true));
		
		mediaSelect.add(new AjaxFormComponentUpdatingBehavior("change"){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				enableTagging(target);
			}
			
		});
		
		PropertyModel<Set<Tag>> selectedTags = new PropertyModel<Set<Tag>>(this, "tags");
		tagPicker = new TagPickerPanel("tagFilters",selectedTags)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onTagsSelectionComplete(AjaxRequestTarget target, Category category, Set<Tag> ts) 
			{
				enableTagging(target);
			}
		};
		add(tagPicker.setOutputMarkupId(true));
	}
	
	private void enableTagging(AjaxRequestTarget target)
	{
		applyTags.setEnabled(media.size()>0 && tags.size()>0);
		target.add(applyTags);
	}

}
