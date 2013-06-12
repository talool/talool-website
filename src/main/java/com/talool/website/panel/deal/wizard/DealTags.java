package com.talool.website.panel.deal.wizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
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
import com.talool.website.models.AvailableDealOffersListModel;
import com.talool.website.models.ModelUtil;
import com.talool.website.panel.deal.DealPreview;

public class DealTags extends DynamicWizardStep
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealTags.class);

	private static final ChoiceRenderer<Tag> choiceRender = new ChoiceRenderer<Tag>("name", "name");

	private List<Tag> tags;

	private final IDynamicWizardStep dealAvailabilityStep;
	private final IDynamicWizardStep createDealOfferStep;
	private DealWizard wizard;

	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public DealTags(IDynamicWizardStep previousStep, DealWizard wiz)
	{
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
		dealAvailabilityStep = new DealAvailability(this, wiz);
		createDealOfferStep = new CreateDealOffer(this, wiz);
		this.wizard = wiz;
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();
		Deal deal = (Deal) getDefaultModelObject();

		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);

		Category cat = ModelUtil.getCategory(deal.getMerchant());

		List<Tag> choices = TagCache.get().getTagsByCategoryName(cat.getName());
		ListMultipleChoice<Tag> tagChoices = new ListMultipleChoice<Tag>("tags", new PropertyModel<List<Tag>>(
				this, "tags"), choices, choiceRender);
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

	@Override
	public boolean isLastStep()
	{
		return false;
	}

	@Override
	public IDynamicWizardStep next()
	{
		Deal deal = (Deal) getDefaultModelObject();
		AvailableDealOffersListModel listModel = new AvailableDealOffersListModel(deal);
		return (listModel.isEmpty()) ? this.createDealOfferStep : this.dealAvailabilityStep;
	}

	@Override
	public IDynamicWizardStep last()
	{
		return new DealSave(this);
	}

}
