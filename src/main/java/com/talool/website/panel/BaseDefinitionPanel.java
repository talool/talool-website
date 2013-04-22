package com.talool.website.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.ServiceException;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen
 * 
 */
abstract public class BaseDefinitionPanel extends BasePanel
{
	private static final long serialVersionUID = -7150011935454160209L;
	private static final Logger LOG = LoggerFactory.getLogger(BaseDefinitionPanel.class);
	protected SubmitCallBack callback;
	protected Form<Void> form;

	public BaseDefinitionPanel(final String id, final SubmitCallBack callback)
	{
		super(id);
		this.callback = callback;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));

		form = new Form<Void>("form");
		add(form);

		form.setDefaultModel(getDefaultCompoundPropertyModel());

		AjaxButton submit = new AjaxButton("submitButton", form)
		{

			private static final long serialVersionUID = -6562989540935949813L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				SessionUtils.errorMessage("The was a problem submitting");
				// attempting to scroll to top
				target.appendJavaScript("$('.content').scrollTop();");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				try
				{
					save();
					target.add(feedback);

					if (callback != null)
					{
						callback.submitSuccess(target);
					}
				}
				catch (ServiceException e)
				{
					SessionUtils.errorMessage("Problem saving account: ", e.getLocalizedMessage());

					LOG.error(e.getLocalizedMessage(), e);
					if (callback != null)
					{
						callback.submitFailure(target);
					}

				}
			}

		};
		form.add(submit);
		submit.add(new Label("submitLabel", getSaveButtonLabel()));
		
		@SuppressWarnings("rawtypes")
		AjaxLink cancel = new AjaxLink("cancelButton")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				if (callback != null)
				{
					callback.submitCancel(target);
				}
			}
			
			
		};
		form.add(cancel);

	}

	abstract public CompoundPropertyModel<?> getDefaultCompoundPropertyModel();

	abstract public String getObjectIdentifier();

	abstract public void save() throws ServiceException;

	abstract public String getSaveButtonLabel();

}
