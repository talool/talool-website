package com.talool.website.marketing.panel;

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
import com.talool.website.marketing.pages.FundraiserInstructions;
import com.talool.website.marketing.pages.FundraiserTracking;
import com.talool.website.models.FundraiserListModel;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.PublisherCobrand;
import com.talool.website.util.SessionUtils;
import com.talool.website.util.WebsiteStatsDClient;
import com.talool.website.util.WebsiteStatsDClient.Action;
import com.talool.website.util.WebsiteStatsDClient.SubAction;
import com.talool.website.validators.EmailValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Date;
import java.util.List;

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
	private Merchant publisher;
	private String cobrand;
	
	public TrackingRegistrationPanel(String id, PublisherCobrand cobrand) {
		super(id);
		
		publisher = cobrand.getPublisher();
		this.cobrand = cobrand.getCobrandName();

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
				target.appendJavaScript("window.oo.stopSpinner()");
				target.add(feedback);
				SessionUtils.errorMessage("There was a problem generating your code.");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{

				if (PermissionUtils.isTrackingOpen(publisher.getId())){
					try
					{
						Merchant school = taloolService.getMerchantById(fundraiser.getMerchantId());
						String code = generateCode(school);
						
						SessionUtils.getSession().success("Success!  You should receive an email shortly.");
						
						PageParameters pageParameters = new PageParameters();
						pageParameters.set("merchant",cobrand);
						pageParameters.set("cobrand","fundraiser"); // extra param that isn't being used
						pageParameters.set("code",code);
						setResponsePage(FundraiserInstructions.class, pageParameters);
					}
					catch (Exception e)
					{
						SessionUtils.errorMessage("There was a problem generating your code.  Please contact support@talool.com for assistance.");
						LOG.error(e.getLocalizedMessage(), e);
						target.appendJavaScript("window.oo.stopSpinner()");
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
		
		EmailTextField em = new EmailTextField("email",new PropertyModel<String>(this,"email"));
		em.add(new EmailValidator());
		form.add(em.setRequired(true).setOutputMarkupId(true));
		
		
		WebMarkupContainer schoolContainer = new WebMarkupContainer("schoolContainer");
		form.add(schoolContainer);
		if (fundraiser==null)
		{
			form.add(new WebMarkupContainer("preselectedSchool").setVisible(false));
		}
		else
		{
			schoolContainer.setVisible(false);
			Label preselectedSchool = new Label("preselectedSchool",fundraiser.getName());
			form.add(preselectedSchool);
		}
		
		
		FundraiserListModel choices = new FundraiserListModel();
		choices.setPublisherId(publisher.getId());
		FundraiserSelect fs = new FundraiserSelect("fundraiser", new PropertyModel<MerchantSummary>(this, "fundraiser"), choices);
		schoolContainer.add(fs.setRequired(true));
		
		container.add(new Label("p1",publisher.getName()));
		container.add(new Label("p2",publisher.getName()));
		container.add(new Label("p3",publisher.getName()));
		form.add(new Label("p5",publisher.getName()));
	}
	
	private String generateCode(Merchant school) throws ServiceException
	{
		// generate the code
		Long merchantAccountId = publisher.getMerchantAccounts().iterator().next().getId();
		MerchantCodeGroup merchantCodeGrp = taloolService.createMerchantCodeGroup(school,
				merchantAccountId, publisher.getId(), fullName, email, (short) 1);
		String code = merchantCodeGrp.getCodes().iterator().next().getCode();
		

		// send email
		String url = getTrackingUrl(code);
		EmailTrackingCodeEntity entity = new EmailTrackingCodeEntity(merchantCodeGrp, publisher, url, cobrand);
		emailService.sendTrackingCodeEmail(new EmailRequestParams<EmailTrackingCodeEntity>(entity));
		
		WebsiteStatsDClient.get().count(Action.merchant_code_group, SubAction.create, fundraiser.getMerchantId());

		return code;
	}
	
	private String getTrackingUrl(String code)
	{
		PageParameters pageParameters = getPage().getPageParameters();
		if (pageParameters.getIndexedCount()<2)
		{
			pageParameters.set(0, "sales");// bogus co-brand
			pageParameters.set(1, "awesome");// bogus co-brand
		}
		pageParameters.set(2, code);
		
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
	
	public void setFundraiserName(String name)
	{
		FundraiserListModel choices = new FundraiserListModel();
		choices.setPublisherId(publisher.getId());
		List<MerchantSummary> fundraisers = choices.getObject();
		for (MerchantSummary f:fundraisers)
		{
			String fn = StringUtils.deleteWhitespace(f.getName());
			if (fn.equalsIgnoreCase(name))
			{
				fundraiser = f;
				break;
			}
		}
	}

}
