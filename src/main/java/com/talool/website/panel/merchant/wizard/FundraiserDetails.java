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

public class FundraiserDetails extends WizardStep
{

	private static final long serialVersionUID = 1L;

	public FundraiserDetails()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		addOrReplace(descriptionPanel.setOutputMarkupId(true));

		// TODO check for duplicate fundraiser names
		descriptionPanel.add(new TextField<String>("name").setRequired(true));

	}

}
