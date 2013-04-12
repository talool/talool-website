package com.talool.website.panel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.ServiceException;

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
	private boolean isNew = false;


	public BaseDefinitionPanel(final String id, final SubmitCallBack callback, boolean isNew)
	{
		super(id);
		this.callback = callback;
		this.isNew = isNew;
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
				// attempting to scroll to top
				target.appendJavaScript("$('.content').scrollTop();");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				StringBuilder sb = new StringBuilder();

				try
				{
					save();
					target.add(feedback);
					sb.append("Successfully ").append(isNew ? "created '" : "updated '")
							.append(getObjectIdentifier());
					getSession().info(sb.toString());
					callback.submitSuccess(target);
				}
				catch (ServiceException e)
				{
					sb.append("Problem saving account: ").append(e.getLocalizedMessage());
					getSession().error(sb.toString());
					LOG.error(sb.toString());
					callback.submitFailure(target);
				}
			}

		};
		
		form.add(submit);
		submit.add(new AttributeModifier("value", getSaveButtonLabel()));
		
	}
	
	abstract public CompoundPropertyModel<?> getDefaultCompoundPropertyModel();
	abstract public String getObjectIdentifier();
	abstract public void save() throws ServiceException;
	abstract public String getSaveButtonLabel();

}
