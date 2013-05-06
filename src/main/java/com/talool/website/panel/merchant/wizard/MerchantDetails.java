package com.talool.website.panel.merchant.wizard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.cache.TagCache;
import com.talool.core.Category;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Image;
import com.talool.core.Merchant;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.domain.ImageImpl;
import com.talool.website.models.CategoryListModel;
import com.talool.website.models.CategoryTagListModel;
import com.talool.website.models.ModelUtil;
import com.talool.website.pages.UploadPage;

public class MerchantDetails extends WizardStep {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDetails.class);
	
	private ChoiceRenderer<Tag> tagChoiceRenderer = new ChoiceRenderer<Tag>("name", "name");
	private ChoiceRenderer<Category> categoryChoiceRenderer = new ChoiceRenderer<Category>("name",
			"name");

	private Category category;
	private List<Tag> tags;
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	public MerchantDetails()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		final Merchant merchant = (Merchant) getDefaultModelObject();
		if (merchant.getId() != null)
		{
			try
			{
				taloolService.merge(merchant);
			}
			catch (ServiceException se) 
		    {
		    	LOG.error("There was an exception merging the merchant: ", se);
		    }
		}
		
		WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		addOrReplace(descriptionPanel.setOutputMarkupId(true));

		// TODO check for duplicate merchant names
		descriptionPanel.add(new TextField<String>("name").setRequired(true));

		final ListMultipleChoice<Tag> tagChoices = new ListMultipleChoice<Tag>("tags",
				new PropertyModel<List<Tag>>(this, "tags"), new CategoryTagListModel(getCategory()),
				tagChoiceRenderer);

		tagChoices.setMaxRows(14);
		tagChoices.setOutputMarkupId(true);
		descriptionPanel.add(tagChoices.setRequired(true));

		DropDownChoice<Category> categorySelect = new DropDownChoice<Category>("category",
				new PropertyModel<Category>(this, "category"), new CategoryListModel(),
				categoryChoiceRenderer);

		categorySelect.setOutputMarkupId(true);
		categorySelect.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{

			private static final long serialVersionUID = -1909537074284102774L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				tagChoices.setChoices(TagCache.get().getTagsByCategoryName(category.getName()));
				target.add(tagChoices);
			}

		});
		descriptionPanel.add(categorySelect.setRequired(true));
	}
	
	public List<Tag> getTags()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		return ModelUtil.getTagList(merch);
	}

	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
		final Merchant merch = (Merchant) getDefaultModelObject();
		Set<Tag> stags = new HashSet<Tag>();
		stags.addAll(tags);
		merch.setTags(stags);
	}

	public Category getCategory()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		return ModelUtil.getCategory(merch);
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}
	
}

