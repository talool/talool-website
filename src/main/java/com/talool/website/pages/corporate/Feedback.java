package com.talool.website.pages.corporate;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.FactoryManager;
import com.talool.core.service.EmailService;
import com.talool.core.service.ServiceException;
import com.talool.service.mail.EmailParams;
import com.talool.service.mail.EmailRequest;

/**
 * Feedback page for mobile customers
 * 
 * @author dmccuen,clintz
 * 
 */
public class Feedback extends BaseCorporatePage
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(Feedback.class);
	private static final String feedbackTitle = "Customer Feedback";
	private static final String feedbackRecipient = "feedback@talool.com";
	protected transient static final EmailService emailService = FactoryManager.get()
			.getServiceFactory().getEmailService();
	private String fromEmail;
	private String feedbackSource;
	private boolean showThanks = false;

	public Feedback(PageParameters params)
	{
		super(params);

		StringValue fe = params.get("fromEmail");
		StringValue fs = params.get("feedbackSrc");
		if (fe.isEmpty())
		{
			// Check the post params
			IRequestParameters postParams = this.getRequest().getPostParameters();
			fe = postParams.getParameterValue("fromEmail");
			fs = postParams.getParameterValue("feedbackSrc");
			fromEmail = fe.toString();
			feedbackSource = fs.toString();
			StringValue feedback = postParams.getParameterValue("feedback");
			if (!feedback.isEmpty())
			{
				StringBuilder feedbackBuilder = new StringBuilder();
				feedbackBuilder.append("From: ").append(fromEmail).append("<br/>");
				feedbackBuilder.append("Source: ").append(feedbackSource).append("<br/><br/>");
				feedbackBuilder.append("Feedback: ").append(feedback.toString()).append("<br/><br/>");

				final EmailRequest<Customer> emailRequest = new EmailRequest<Customer>();
				emailRequest.setEmailParams(new EmailParams(feedbackTitle, feedbackRecipient, fromEmail, feedbackBuilder.toString()));
				emailRequest.setCategory("feedback");

				try
				{
					emailService.sendEmail(emailRequest);
					LOG.info("Feedback sent succcessfully for :" + emailRequest.getEmailParams().getFrom());
				}
				catch (ServiceException se)
				{
					LOG.error("Failed sending feedback", se);
				}
				catch (Exception e)
				{
					LOG.error("Failed sending feedback", e);
				}
				showThanks = true;
			}
		}
		else
		{
			fromEmail = fe.toString();
			feedbackSource = fs.toString();
			showThanks = false;
		}

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		WebMarkupContainer form = new WebMarkupContainer("formbox");
		add(form);
		HiddenField<String> email = new HiddenField<String>("fromEmail", new PropertyModel<String>(this, "fromEmail"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getInputName()
			{
				return "fromEmail";
			}
		};
		form.add(email);
		HiddenField<String> src = new HiddenField<String>("feedbackSrc", new PropertyModel<String>(this, "feedbackSource"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getInputName()
			{
				return "feedbackSrc";
			}
		};
		form.add(src);

		form.setVisible(!showThanks);

		if (showThanks)
		{
			add(new Label("header", "Thanks For Your Feedback!"));
		}
		else
		{
			add(new Label("header", "We're Listening"));
		}
	}

}
