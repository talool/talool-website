package com.talool.website.marketing.panel;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.UrlValidator;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantLocation;
import com.talool.core.service.EmailService;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.ErrorCode;
import com.talool.service.ServiceConfig;
import com.talool.service.mail.EmailRequestParams;
import com.talool.utils.HttpUtils;
import com.talool.utils.KeyValue;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.SessionUtils;
import com.talool.website.validators.EmailValidator;
import com.vividsolutions.jts.geom.Point;

public class RegistrationPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(RegistrationPanel.class);
	private static final String talool = "Talool";
	private MerchantAccount _taloolMerchantAccount;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	protected transient static final EmailService emailService = FactoryManager.get()
			.getServiceFactory().getEmailService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	private String merchantName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String confirm;
	private String roleTitle;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String website;
	
	private boolean isFundraiser;
	private boolean isPublisher;

	private boolean acceptedMA;
	private boolean acceptedTOS;
	
	public RegistrationPanel(String id, boolean isFundraiser, boolean isPublisher) {
		super(id);
		this.isFundraiser = isFundraiser;
		this.isPublisher = isPublisher;
		
		try
		{
			_taloolMerchantAccount = taloolService.getMerchantAccountById(ServiceConfig.get().getTaloolPublisherMerchantAccountId());
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get the taloolMerchantAccountId", se);
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
				if (!acceptedMA)
				{
					SessionUtils.errorMessage("Please accept the Merchant Agreement");
				}
				else if (!acceptedTOS)
				{
					SessionUtils.errorMessage("Please accept the Terms of Service");
				}
				else
				{
					try
					{
						save();
						merchantName="";
						address1="";
						address2="";
						city="";
						zip="";
						phone="";
						website="";
						roleTitle="";
						firstName="";
						lastName="";
						email="";
						acceptedMA=false;
						acceptedTOS=false;
						target.add(container);
					}
					catch (Exception e)
					{
						SessionUtils.errorMessage("There was a problem saving your account.  Please contact support@talool.com for assistance.");
						LOG.error(e.getLocalizedMessage(), e);
					}
				}
				
				target.add(feedback);
			}

		};
		form.add(submit);
		
		TextField<String> mn = new TextField<String>("merchantName",new PropertyModel<String>(this,"merchantName"));
		form.add(mn.setRequired(true).setOutputMarkupId(true));
		
		TextField<String> a1 = new TextField<String>("address1",new PropertyModel<String>(this,"address1"));
		form.add(a1.setRequired(true).setOutputMarkupId(true));
		
		TextField<String> a2 = new TextField<String>("address2",new PropertyModel<String>(this,"address2"));
		form.add(a2);
		
		TextField<String> city = new TextField<String>("city",new PropertyModel<String>(this,"city"));
		form.add(city.setRequired(true).setOutputMarkupId(true));
		
		StateSelect state = new StateSelect("state", new PropertyModel<StateOption>(this, "stateOption"));
		form.add(state.setRequired(true));
		
		TextField<String> zp = new TextField<String>("zip",new PropertyModel<String>(this,"zip"));
		form.add(zp.setRequired(true).setOutputMarkupId(true));
		
		TextField<String> ph = new TextField<String>("phone",new PropertyModel<String>(this,"phone"));
		form.add(ph.setRequired(true).setOutputMarkupId(true));
		
		TextField<String> web = new TextField<String>("website",new PropertyModel<String>(this,"website"));
		web.add(new UrlValidator());
		form.add(web.setRequired(true));
		
		TextField<String> rt = new TextField<String>("roleTitle",new PropertyModel<String>(this,"roleTitle"));
		form.add(rt.setRequired(true).setOutputMarkupId(true));
		
		TextField<String> fn = new TextField<String>("firstName",new PropertyModel<String>(this,"firstName"));
		form.add(fn.setRequired(true).setOutputMarkupId(true));
		
		TextField<String> ln = new TextField<String>("lastName",new PropertyModel<String>(this,"lastName"));
		form.add(ln.setRequired(true).setOutputMarkupId(true));
		
		TextField<String> em = new TextField<String>("email",new PropertyModel<String>(this,"email"));
		em.add(new EmailValidator());
		form.add(em.setRequired(true).setOutputMarkupId(true));
		
		CheckBox tosCheckbox = new CheckBox("tos", new PropertyModel<Boolean>(this, "acceptedTOS"));
		form.add(tosCheckbox);
		
		CheckBox merchantAgreementCheckbox = new CheckBox("merchantAgreement", new PropertyModel<Boolean>(this, "acceptedMA"));
		form.add(merchantAgreementCheckbox);
		
		FormComponent<String> pw1 = new PasswordTextField("password",new PropertyModel<String>(this,"password")).setRequired(true);
		FormComponent<String> pw2 = new PasswordTextField("confirm", new PropertyModel<String>(this,"confirm")).setRequired(true);
		EqualPasswordInputValidator pwv = new EqualPasswordInputValidator(pw1, pw2);
		form.add(pw1);
		form.add(pw2);
		form.add(pwv);
	}
	
	public StateOption getStateOption()
	{
		return StateSelect.getStateOptionByCode(state);

	}

	public void setStateOption(final StateOption stateOption)
	{
		state = stateOption.getCode();
	}
	
	private void save() throws ServiceException
	{
		// TODO create a simple check for existing merchants by name and merchant account email
		
		// create merchant
		Merchant merchant = domainFactory.newMerchant();
		merchant.setName(merchantName);
		merchant.getProperties().createOrReplace(KeyValue.publisher, isPublisher);
		merchant.getProperties().createOrReplace(KeyValue.fundraiser, isFundraiser);
		merchant.getProperties().createOrReplace(KeyValue.merchantTermsAcceptedV1, acceptedTOS);
		merchant.getProperties().createOrReplace(KeyValue.merchantAgreementAcceptedV1, acceptedMA);
		
		// create merchant location
		merchant.getPrimaryLocation().setAddress1(address1);
		merchant.getPrimaryLocation().setAddress2(address2);
		merchant.getPrimaryLocation().setCity(city);
		merchant.getPrimaryLocation().setStateProvinceCounty(state);
		merchant.getPrimaryLocation().setZip(zip);
		merchant.getPrimaryLocation().setPhone(phone);
		merchant.getPrimaryLocation().setWebsiteUrl(website);
		merchant.getPrimaryLocation().setEmail(email);
		merchant.getPrimaryLocation().setCreatedByMerchantAccount(_taloolMerchantAccount);
		Point p = getGeometry(merchant.getPrimaryLocation());
		if (p!=null)
		{
			merchant.getPrimaryLocation().setGeometry(p);
		}
		taloolService.save(merchant);
		
		// create merchant account
		MerchantAccount account = domainFactory.newMerchantAccount(merchant);
		account.setEmail(email);
		account.setPassword(password);
		account.setRoleTitle(roleTitle);
		account.setAllowDealCreation(true);
		taloolService.save(account);
		
		// send email
		emailService.sendMerchantAccountEmail(new EmailRequestParams<MerchantAccount>(account));
		
		SessionUtils.successMessage("Your account has been created.  You will receive an email shortly with more information.");
	}
	
	private Point getGeometry(MerchantLocation mloc)
	{
		Point point = null;
		try
		{
			// hit Google
			point = HttpUtils.getGeometry(mloc);
			
		}
		catch(ServiceException se)
		{
			LOG.error("failed to get geo for new merchant location: "+HttpUtils.buildAddress(mloc), se);
			if (se.getErrorCode().equals(ErrorCode.GEOCODER_OVER_QUERY_LIMIT))
			{
				LOG.error("ABORT: "+se.getErrorCode().getMessage());
			}
		}
		catch(Exception e)
		{
			LOG.error("failed to get geo for new merchant location: "+HttpUtils.buildAddress(mloc), e);
		}
		return point;
	}

}
