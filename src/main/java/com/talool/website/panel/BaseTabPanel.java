package com.talool.website.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;
import com.talool.website.pages.BasePage;

/**
 * 
 * @author clintz
 * 
 */
public abstract class BaseTabPanel extends Panel
{
	private static final long serialVersionUID = -2584158965295658902L;

	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public BaseTabPanel(String id)
	{
		super(id);

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		// hook into the action link and update it for this panel
		final BasePage page = (BasePage) this.getPage();
		Label actionLabel = new Label("actionLabel", getActionLabel());
		actionLabel.setOutputMarkupId(true);

		final AdminModalWindow definitionModal = page.getModal();
		final SubmitCallBack callback = page.getCallback(definitionModal);
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 7891264295227523725L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				definitionModal.setTitle(getActionLabel());

				definitionModal.setContent(getNewDefinitionPanel(definitionModal.getContentId(), callback));
				definitionModal.show(target);
			}
		};
		actionLink.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		page.setActionLink(actionLink);

	}

	abstract public String getActionLabel();

	abstract public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback);

}
