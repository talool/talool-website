package com.talool.website.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;
import com.talool.website.panel.AdminMenuPanel;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.panel.SubmitCallBack;

/**
 * 
 * @author clintz
 * 
 */
public abstract class BasePage extends WebPage
{
	private static final long serialVersionUID = -7463278066879672957L;

	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public FeedbackPanel feedback;

	private AdminModalWindow _modal;

	private WebMarkupContainer _action;
	private AjaxLink<Void> _actionLink;
	private Label _actionLabel;

	public BasePage()
	{
		super();
		init();
	}

	public BasePage(IModel<?> model)
	{
		super(model);
		init();
	}

	public BasePage(PageParameters parameters)
	{
		super(parameters);
		init();
	}

	private void init()
	{
		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
		add(new Label("headerTitle", getHeaderTitle()));

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));

		final AdminModalWindow definitionModal = new AdminModalWindow("modal");
		add(definitionModal.setOutputMarkupId(true));
		final SubmitCallBack callback = getCallback(definitionModal);

		// Get the definition panel from the subclass and add it to the definition
		// modal
		final Panel definitionPanel = getNewDefinitionPanel(definitionModal.getContentId(), callback);
		if (definitionPanel != null)
		{
			definitionModal.setContent(definitionPanel.setOutputMarkupId(true));
		}

		_modal = definitionModal;

		_actionLink = new AjaxLink<Void>("actionLink")
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

		_action = new WebMarkupContainer("action");
		_actionLabel = new Label("actionLabel", getNewDefinitionPanelTitle());
		_actionLabel.setOutputMarkupId(true);
		_actionLink.add(_actionLabel);
		_actionLink.setOutputMarkupId(true);
		_action.add(_actionLink);
		add(_action);
	}

	public SubmitCallBack getCallback(final AdminModalWindow modal)
	{
		SubmitCallBack callback = new SubmitCallBack()
		{

			private static final long serialVersionUID = 6420614586937543567L;

			@Override
			public void submitSuccess(AjaxRequestTarget target)
			{
				modal.close(target);
				target.add(BasePage.this);
			}

			@Override
			public void submitFailure(AjaxRequestTarget target)
			{

			}
		};
		return callback;
	}

	public FeedbackPanel getFeedback()
	{
		return feedback;
	}

	public AdminModalWindow getModal()
	{
		return _modal;
	}

	public String getHeaderTitle()
	{
		return "You need to override getHeaderTitle()";
	}

	public AjaxLink<Void> getActionLink()
	{
		return _actionLink;
	}

	public void setActionLink(AjaxLink<Void> link)
	{
		_actionLink.replaceWith(link);
		_actionLink = link;
	}

	abstract public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback);

	abstract public String getNewDefinitionPanelTitle();

}
