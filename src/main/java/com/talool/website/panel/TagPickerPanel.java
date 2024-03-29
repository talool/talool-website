package com.talool.website.panel;

import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Category;
import com.talool.core.Tag;
import com.talool.website.models.CategoryListModel;
import com.talool.website.models.CategoryTagListModel;

abstract public class TagPickerPanel extends Panel
{

	private static final long serialVersionUID = -4183138880833396304L;
	private static final Logger LOG = LoggerFactory.getLogger(TagPickerPanel.class);
	
	protected static final ChoiceRenderer<Tag> choiceRender = new ChoiceRenderer<Tag>("name", "name");
	protected ChoiceRenderer<Category> categoryChoiceRenderer = new ChoiceRenderer<Category>("name",
			"name");
	protected Category category;
	
	private IModel<Set<Tag>> tags;
	private ListMultipleChoice<Tag> tagChoices;

	public TagPickerPanel(String id, Category cat, IModel<Set<Tag>> tags)
	{
		super(id);
		if (cat==null)
		{
			cat = (new CategoryListModel()).getObject().get(0);
		}
		category = cat;
		this.tags = tags;
	}
	
	public TagPickerPanel(String id, IModel<Set<Tag>> tags)
	{
		super(id);
		category = (new CategoryListModel()).getObject().get(0);
		this.tags = tags;
	}
	
	public void addChoices()
	{
		CategoryTagListModel tagsModel = new CategoryTagListModel(category);
		
		tagChoices = new ListMultipleChoice<Tag>("tags", tags, tagsModel.getObject(), choiceRender);
		
		tagChoices.setMaxRows(25);
		tagChoices.setOutputMarkupId(true);
		
		add(tagChoices.setRequired(true));
		
		tagChoices.add(new AjaxFormComponentUpdatingBehavior("change")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				onTagsSelectionComplete(target, category, tags.getObject());
			}
			
		});
		
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		addChoices();
		
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
				setChoices(target);
			}

		});
		add(categorySelect);
	}
	
	protected void setChoices(AjaxRequestTarget target)
	{
		CategoryTagListModel tagsModel = new CategoryTagListModel(category);
		tagChoices.setChoices(tagsModel.getObject());
		target.add(tagChoices);
	}

	abstract public void onTagsSelectionComplete(AjaxRequestTarget target, Category category, Set<Tag> tags);

}
