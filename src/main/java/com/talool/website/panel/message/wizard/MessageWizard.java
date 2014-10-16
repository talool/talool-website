package com.talool.website.panel.message.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.jquery.ui.widget.wizard.AbstractWizard;
import com.talool.core.Customer;
import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.messaging.MessagingFactory;
import com.talool.messaging.job.DealOfferPurchaseJob;
import com.talool.service.ServiceFactory;
import com.talool.utils.KeyValue;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.message.MerchantGift;
import com.talool.website.util.MerchantAccountComparator;
import com.talool.website.util.SessionUtils;
import com.talool.website.util.TaloolWizardModel;

public class MessageWizard extends AbstractWizard<MerchantGift>
{

	private static final long serialVersionUID = 5336259325674186395L;
	private static final Logger LOG = LoggerFactory.getLogger(MessageWizard.class);
	protected transient static final CustomerService customerService = FactoryManager.get().getServiceFactory().getCustomerService();
	protected transient static final TaloolService taloolService = FactoryManager.get().getServiceFactory().getTaloolService();

	public MessageWizard(String id, String title)
	{
		super(id, title);

		WizardModel wizardModel = new TaloolWizardModel();
		wizardModel.add(new MessageGiftStep());
		wizardModel.add(new MessageCriteriaStep());
		wizardModel.add(new JobScheduleStep());
		wizardModel.add(new MessageConfirmationStep());

		wizardModel.setLastVisible(false);

		this.init(wizardModel);
	}

	@Override
	public void setModelObject(MerchantGift message)
	{
		/*
		 * Considered setting the model on the form rather than the Wizard so that it would filter down to the steps
		 * directly. However, that didn't work, hence pass off via onActiveStepChanged
		 */
		this.setDefaultModel(new CompoundPropertyModel<MerchantGift>(message));
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
		final MerchantGift mg = (MerchantGift) getDefaultModelObject();

		try
		{
			MerchantAccount merchantAccount = null;
			Merchant merchant = null;
			// TODO fix this MerchantAccount selection hack
			// If the merchant has no merchant accounts, the message will be associated with the logged in user
			try
			{
				merchant = taloolService.getMerchantById(mg.getMerchant().getId());
				Set<MerchantAccount> mas = merchant.getMerchantAccounts();
				if (mas.isEmpty() == false)
				{
					List<MerchantAccount> mal = new ArrayList<MerchantAccount>();
					mal.addAll(mas);
					MerchantAccountComparator mac = new MerchantAccountComparator(MerchantAccountComparator.ComparatorType.CreateDate);
					Collections.sort(mal, mac);
					merchantAccount = mal.get(0);
				}
			}
			catch (ServiceException se)
			{
				LOG.error("Failed to get merchant account", se);
			}
			if (merchantAccount == null)
				merchantAccount = SessionUtils.getSession().getMerchantAccount();

			Customer fromCustomer = taloolService.getCustomerForMerchant(mg.getMerchant());
			List<Customer> targetedCustomers = customerService.getCustomers(mg.getCriteria());

			//MerchantGiftJob job = MessagingFactory.newMerchantGiftJob(merchant, merchantAccount, fromCustomer, mg.getDeal(),
			//		mg.getStartDate(), mg.getTitle());
			
			DealOffer offer = taloolService.getDealOffers().get(0);
			DealOfferPurchaseJob job = MessagingFactory.newDealOfferPurchaseJob(merchant, merchantAccount, fromCustomer, offer, mg.getStartDate(), "Testing");
			job.getProperties().createOrReplace(KeyValue.dealOfferPurchaseJobNotesKey, "TODO: get a custom string from the publisher");
			
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
