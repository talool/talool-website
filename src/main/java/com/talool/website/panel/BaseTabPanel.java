package com.talool.website.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;

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
	
	private AdminModalWindow _modal;
	private WebMarkupContainer _container;

	public BaseTabPanel(String id)
	{
		super(id);

	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));

		final AdminModalWindow definitionModal = new AdminModalWindow("modal");
		container.add(definitionModal);
		
		final SubmitCallBack callback = getCallback(container, definitionModal);
		
		// Get the definition panel from the subclass and add it to the definition modal
		final Panel definitionPanel = getNewDefinitionPanel(definitionModal.getContentId(), callback);
		definitionModal.setContent(definitionPanel);
		
		_modal = definitionModal;
		_container = container;
		
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 7891264295227523725L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				definitionModal.setTitle(getNewDefinitionPanelTitle());
				definitionModal.setContent(getNewDefinitionPanel(definitionModal.getContentId(), callback));
				definitionModal.show(target);
			}
		};
		actionLink.add(new Label("actionLabel",getNewDefinitionPanelTitle()));
		container.add(actionLink);
		
	}

	public AdminModalWindow getModal() {
		return _modal;
	}
	
	public WebMarkupContainer getContainer() {
		return _container;
	}
	
	public SubmitCallBack getCallback(final WebMarkupContainer container, final AdminModalWindow modal)
	{
		SubmitCallBack callback = new SubmitCallBack()
		{

			private static final long serialVersionUID = 6420614586937543567L;

			@Override
			public void submitSuccess(AjaxRequestTarget target)
			{
				modal.close(target);
				target.add(container);
			}

			@Override
			public void submitFailure(AjaxRequestTarget target)
			{

			}
		};
		return callback;
	}
	
	abstract public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback);
	abstract public String getNewDefinitionPanelTitle();
	
}
