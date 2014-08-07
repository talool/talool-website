package com.talool.website.panel.message;

import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantPanel;
import com.talool.website.panel.message.wizard.MessageWizard;

public class MerchantMessages extends BaseTabPanel {


	private static final Logger LOG = LoggerFactory.getLogger(MerchantPanel.class);
	private static final long serialVersionUID = 1177862345946906145L;
	private UUID _merchantId;
	private MessageWizard wizard;
	
	public MerchantMessages(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final BasePage page = (BasePage) getPage();
		
		final WebMarkupContainer container = new WebMarkupContainer("list");
		container.setOutputMarkupId(true);
		add(container);
		
		// override the action button
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();

				Merchant merchant = new MerchantModel(_merchantId, true).getObject();
				MerchantGift mg = new MerchantGift(merchant);
				wizard.setModelObject(mg);
				wizard.open(target);
			}
		};
		page.setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getActionLabel());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);
		
		// Wizard
		wizard = new MessageWizard("messageWiz", "Message Wizard")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				// refresh the list after a message is created
				target.add(container);
			}
		};
		addOrReplace(wizard.setOutputMarkupId(true));
	}
	
	@Override
	public String getActionLabel() {
		return "Create Message";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		return null;
	}

	

}
