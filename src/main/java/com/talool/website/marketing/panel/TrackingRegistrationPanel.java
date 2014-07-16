package com.talool.website.marketing.panel;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantCodeGroup;
import com.talool.core.service.EmailService;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.ServiceFactory;
import com.talool.stats.MerchantSummary;
import com.talool.website.component.FundraiserSelect;
import com.talool.website.models.FundraiserListModel;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.SessionUtils;

public class TrackingRegistrationPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(TrackingRegistrationPanel.class);

	private static final String payback = "Payback Book";
	
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
	
	public TrackingRegistrationPanel(String id) {
		super(id);
		
		try
		{
			// get the payback id
			List<Merchant> teds = ServiceFactory.get().getTaloolService().getMerchantByName(payback);
			Merchant ted = teds.get(0);
			publisherId = ted.getId();
			merchantAccountId = ted.getMerchantAccounts().iterator().next().getId();
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to find payback", se);
		}
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

		};
		form.add(submit);
		
		TextField<String> mn = new TextField<String>("fullName",new PropertyModel<String>(this,"fullName"));
		form.add(mn.setRequired(true).setOutputMarkupId(true));
		
		FundraiserListModel choices = new FundraiserListModel();
		choices.setPublisherId(publisherId);
		FundraiserSelect fs = new FundraiserSelect("fundraiser", new PropertyModel<MerchantSummary>(this, "fundraiser"), choices);
		form.add(fs.setRequired(true));
	}
	
	private void generateCode() throws ServiceException
	{
		// generate the code
		StringBuilder title = new StringBuilder();
		title.append(fundraiser.getName()).append(" Tracking Codes");
		String notes = fullName; // TODO add email to this?
		Merchant school = taloolService.getMerchantById(fundraiser.getMerchantId());
		MerchantCodeGroup merchantCodeGrp = taloolService.createMerchantCodeGroup(school,
				merchantAccountId, publisherId, title.toString(), notes, (short) 1);
		
		String code = merchantCodeGrp.getCodes().iterator().next().getCode();;
		success("Your tracking code is "+code);
	}

}
