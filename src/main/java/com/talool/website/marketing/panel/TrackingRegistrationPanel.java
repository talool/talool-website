package com.talool.website.marketing.panel;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantCodeGroup;
import com.talool.core.service.EmailService;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.mail.EmailRequestParams;
import com.talool.service.mail.EmailTrackingCodeEntity;
import com.talool.stats.MerchantSummary;
import com.talool.website.component.FundraiserSelect;
import com.talool.website.marketing.pages.FundraiserTracking;
import com.talool.website.models.FundraiserListModel;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SessionUtils;
import com.talool.website.validators.EmailValidator;

public class TrackingRegistrationPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(TrackingRegistrationPanel.class);
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	
	protected transient static final EmailService emailService = FactoryManager.get()
			.getServiceFactory().getEmailService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	private MerchantSummary fundraiser;
	private String fullName;
	private String email;
	
	private UUID publisherId;
	private long merchantAccountId;
	private String publisherName;
	
	public TrackingRegistrationPanel(String id, Merchant publisher, long maId) {
		super(id);
		
		publisherId = publisher.getId();
		merchantAccountId = maId;
		publisherName = publisher.getName();
	}
	
	@Override
	protected void onInitialize() 
	{
		super.onInitialize();
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		container.add(feedback.setOutputMarkupId(true));
		
		final Form<Void> form = new Form<Void>("form");
		container.add(form);
		
		AjaxButton submit = new AjaxButton("submitButton", form)
		{

			private static final long serialVersionUID = -6562989540935949813L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				SessionUtils.errorMessage("There was a problem saving your account");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{

				if (PermissionUtils.isTrackingOpen(publisherId)){
					try
					{
						generateCode();
						fundraiser = null;
						fullName = "";
						email="";
						target.add(container);
					}
					catch (Exception e)
					{
						SessionUtils.errorMessage("There was a problem generating your code.  Please contact support@talool.com for assistance.");
						LOG.error(e.getLocalizedMessage(), e);
					}
					
					target.add(feedback);
				}
				else
				{
					// reload the page to show the error message
					long ts = (new Date()).getTime();
					target.appendJavaScript("document.location.href = document.location.href + '?' +"+ts);
				}
				
			}

		};
		form.add(submit);
		
		TextField<String> mn = new TextField<String>("fullName",new PropertyModel<String>(this,"fullName"));
		form.add(mn.setRequired(true).setOutputMarkupId(true));
		
		TextField<String> em = new TextField<String>("email",new PropertyModel<String>(this,"email"));
		em.add(new EmailValidator());
		form.add(em.setRequired(true).setOutputMarkupId(true));
		
		FundraiserListModel choices = new FundraiserListModel();
		choices.setPublisherId(publisherId);
		FundraiserSelect fs = new FundraiserSelect("fundraiser", new PropertyModel<MerchantSummary>(this, "fundraiser"), choices);
		form.add(fs.setRequired(true));
		
		container.add(new Label("p1",publisherName));
		container.add(new Label("p2",publisherName));
	}
	
	private void generateCode() throws ServiceException
	{
		// generate the code
		Merchant school = taloolService.getMerchantById(fundraiser.getMerchantId());
		MerchantCodeGroup merchantCodeGrp = taloolService.createMerchantCodeGroup(school,
				merchantAccountId, publisherId, fullName, email, (short) 1);
		String code = merchantCodeGrp.getCodes().iterator().next().getCode();
		
		StringBuilder message = new StringBuilder("Your tracking code is ");
		message.append(code).append(". An email will be sent to you shortly.");
		
		// send email
		Merchant publisher = taloolService.getMerchantById(publisherId);
		String url = getTrackingUrl(code);
		EmailTrackingCodeEntity entity = new EmailTrackingCodeEntity(merchantCodeGrp, publisher, url);
		emailService.sendTrackingCodeEmail(new EmailRequestParams<EmailTrackingCodeEntity>(entity));
		
		success(message.toString());
	}
	
	private String getTrackingUrl(String code)
	{
		PageParameters pageParameters = getPage().getPageParameters();
		if (pageParameters.getIndexedCount()<1)
		{
			pageParameters.set(0, "talool");// bogus co-brand
		}
		pageParameters.set(1, code);
		
		Url url = RequestCycle.get().getRequest().getUrl();
		StringBuilder u = new StringBuilder();
		u.append(url.getProtocol()).append("://").append(url.getHost());
		if (url.getPort() != 80)
		{
			u.append(":").append(url.getPort());
		}
		
		String path = RequestCycle.get().urlFor(FundraiserTracking.class, pageParameters).toString();
		if (path.indexOf("..")==0)
		{
			path = path.substring(2);
		}
		u.append(path);
		
		return u.toString();
	}

}
