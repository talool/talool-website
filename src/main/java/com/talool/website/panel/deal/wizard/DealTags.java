package com.talool.website.panel.deal.wizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.cache.TagCache;
import com.talool.core.Category;
import com.talool.core.Deal;
import com.talool.core.FactoryManager;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.models.ModelUtil;
import com.talool.website.panel.deal.DealPreview;

public class DealTags extends WizardStep
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealTags.class);
	private List<Tag> tags;

	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public DealTags()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();
		Deal deal = (Deal) getDefaultModelObject();
		// if (deal.getId() != null)
		// {
		// try
		// {
		// taloolService.merge(deal);
		// }
		// catch (ServiceException se)
		// {
		// LOG.error("There was an exception merging the merchant: ", se);
		// }
		// }

		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);

		ChoiceRenderer<Tag> cr = new ChoiceRenderer<Tag>("name", "name");
		Category cat = ModelUtil.getCategory(deal.getMerchant());
		List<Tag> choices = TagCache.get().getTagsByCategoryName(cat.getName());
		ListMultipleChoice<Tag> tagChoices = new ListMultipleChoice<Tag>("tags", new PropertyModel<List<Tag>>(
				this, "tags"), choices, cr);
		tagChoices.setMaxRows(25);
		tagChoices.setOutputMarkupId(true);
		addOrReplace(tagChoices.setRequired(true));

	}

	// TODO consider watching for a change on the tag list to decide if the merge
	// needs to happen
	public List<Tag> getTags()
	{
		Deal deal = (Deal) getDefaultModelObject();
		try
		{
			tags = ModelUtil.getTagList(deal);
		}
		catch (Exception e)
		{
			LOG.debug("Failed to get tags for deal -- trying to reattach");
			tags = getTags(deal, true);
		}
		if (CollectionUtils.isEmpty(tags))
		{
			tags = new ArrayList<Tag>();
		}
		return tags;
	}

	private List<Tag> getTags(Deal deal, boolean reattach)
	{
		List<Tag> tags = null;
		if (reattach)
		{
			try
			{
				taloolService.merge(deal);
			}
			catch (ServiceException se)
			{
				LOG.error("Failed to reattach the Deal before getting tags", se);
			}
		}
		try
		{
			tags = ModelUtil.getTagList(deal);
		}
		catch (Exception e)
		{
			LOG.error("Failed to get tags for deal: ", e);
		}
		return tags;
	}

	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
		Deal deal = (Deal) getDefaultModelObject();
		if (CollectionUtils.isNotEmpty(tags))
		{
			Set<Tag> selectedTags = new HashSet<Tag>();
			selectedTags.addAll(tags);
			deal.setTags(selectedTags);
		}
		else
		{
			deal.clearTags();
		}
	}

}
