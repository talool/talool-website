package com.talool.website.panel.merchant.wizard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.cache.TagCache;
import com.talool.core.Category;
import com.talool.core.Merchant;
import com.talool.core.Tag;
import com.talool.website.models.CategoryListModel;
import com.talool.website.models.CategoryTagListModel;
import com.talool.website.models.ModelUtil;

public class MerchantDetails extends WizardStep
{

	private static final long serialVersionUID = 1L;

	private ChoiceRenderer<Tag> tagChoiceRenderer = new ChoiceRenderer<Tag>("name", "name");
	private ChoiceRenderer<Category> categoryChoiceRenderer = new ChoiceRenderer<Category>("name",
			"name");

	private Category category;
	private List<Tag> tags;

	public MerchantDetails()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

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
				((Merchant) getDefaultModelObject()).setCategory(category);
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
		return merch.getCategory();
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

}
