package com.talool.website.panel.merchant.definition;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;

import com.talool.core.Tag;
import com.talool.website.models.TagListModel;
import com.talool.website.models.TagListModel.CATEGORY_CONTEXT;

public class CategoryUpdatingBehavior extends AjaxFormComponentUpdatingBehavior  {
	
	private static final long serialVersionUID = -7587523609699653228L;

	private ListMultipleChoice<List<Tag>> tags;
	
	public CategoryUpdatingBehavior(String event, ListMultipleChoice<List<Tag>> tags) {
		super(event);
		this.tags = tags;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		
		IModel<List<Tag>> model;
		String category = getFormComponent().getValue();
		
		if (CATEGORY_CONTEXT.DINE_IN.equalsName(category) || 
			CATEGORY_CONTEXT.CASUAL.equalsName(category)) 
		{
			model = new TagListModel(CATEGORY_CONTEXT.FOOD);
		} 
		else if (CATEGORY_CONTEXT.SHOPPING_SERVICES.equalsName(category)) 
		{
			model = new TagListModel(CATEGORY_CONTEXT.SHOPPING_SERVICES);
		}
		else if (CATEGORY_CONTEXT.FUN.equalsName(category)) 
		{
			model = new TagListModel(CATEGORY_CONTEXT.FUN);
		}
		else 
		{
			model = new TagListModel(CATEGORY_CONTEXT.NIGHTLIFE);
		}
		
		tags.setChoices( (IModel<? extends List<? extends List<Tag>>>) model);
		tags.modelChanged();
		target.add(tags);
	}
}
