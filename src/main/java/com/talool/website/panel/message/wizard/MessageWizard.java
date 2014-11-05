package com.talool.website.panel.message.wizard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.jquery.ui.widget.wizard.AbstractWizard;
import com.talool.core.Customer;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.messaging.MessagingFactory;
import com.talool.messaging.job.MessagingJob;
import com.talool.service.ServiceFactory;
import com.talool.utils.KeyValue;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.message.MessageJobPojo;
import com.talool.website.panel.message.MessageJobPojo.MessageJobType;
import com.talool.website.util.SessionUtils;
import com.talool.website.util.TaloolDynamicWizardModel;

public class MessageWizard extends AbstractWizard<MessageJobPojo>
{

	private static final long serialVersionUID = 5336259325674186395L;
	private static final Logger LOG = LoggerFactory.getLogger(MessageWizard.class);
	protected transient static final CustomerService customerService = FactoryManager.get().getServiceFactory().getCustomerService();
	protected transient static final TaloolService taloolService = FactoryManager.get().getServiceFactory().getTaloolService();

	public MessageWizard(String id, String title)
	{
		super(id, title);
		
		final DynamicWizardModel wizardModel = new TaloolDynamicWizardModel(new MessageTypeStep());

		wizardModel.setLastVisible(false);

		this.init(wizardModel);
	}

	@Override
	public void setModelObject(MessageJobPojo message)
	{
		/*
		 * Considered setting the model on the form rather than the Wizard so that it would filter down to the steps
		 * directly. However, that didn't work, hence pass off via onActiveStepChanged
		 */
		this.setDefaultModel(new CompoundPropertyModel<MessageJobPojo>(message));
	}

	@Override
	public void onActiveStepChanged(IWizardStep step)
	{
		super.onActiveStepChanged(step);
		/*
		 * Set the model on the active set. The steps can't share the wizard's model directly. This call happens after
		 * onInitialize, and before onConfigure.
		 */
		((WizardStep) step).setDefaultModel(getDefaultModel());
	}

	@Override
	protected void onFinish(AjaxRequestTarget target)
	{
		/*
		 * Save & Send the message
		 */
		final MessageJobPojo messageJob = (MessageJobPojo) getDefaultModelObject();

		try
		{
			MerchantAccount merchantAccount = SessionUtils.getSession().getMerchantAccount();
			Merchant merchant = merchantAccount.getMerchant();

			Customer fromCustomer = taloolService.getCustomerForMerchant(messageJob.getMerchant());
			List<Customer> targetedCustomers = customerService.getCustomers(messageJob.getCriteria());

			MessagingJob job;
			if (messageJob.getJobType() == MessageJobType.MerchantGiftJob)
			{
				job = MessagingFactory.newMerchantGiftJob(merchant, merchantAccount, fromCustomer, messageJob.getDeal(),
						messageJob.getStartDate(), messageJob.getTitle());
			}
			else
			{
				job = MessagingFactory.newDealOfferPurchaseJob(merchant, merchantAccount, fromCustomer, 
						messageJob.getOffer(), messageJob.getStartDate(), messageJob.getTitle());
				job.getProperties().createOrReplace(KeyValue.dealOfferPurchaseJobNotesKey, messageJob.getMessage());
			}
			
			ServiceFactory.get().getMessagingService().scheduleMessagingJob(job, targetedCustomers);

			LOG.debug("message sent");
		}
		catch (ServiceException e)
		{
			LOG.error("Failed to send message", e);
		}

		target.add(((BasePage) getPage()).feedback.setEscapeModelStrings(false));
	}

	@Override
	protected void onCancel(AjaxRequestTarget target)
	{
		target.add(((BasePage) getPage()).feedback);
	}

	@Override
	public int getWidth()
	{
		return 890;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target)
	{
		super.onSubmit(target);
		target.add(this.getFeedbackPanel());
	}
}
