package com.talool.website.panel.message.wizard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.jquery.ui.widget.wizard.AbstractWizard;
import com.talool.core.Customer;
import com.talool.core.FactoryManager;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.message.MerchantGift;
import com.talool.website.util.TaloolWizardModel;

public class MessageWizard extends AbstractWizard<MerchantGift> {
	
	private static final long serialVersionUID = 5336259325674186395L;
	private static final Logger LOG = LoggerFactory.getLogger(MessageWizard.class);
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public MessageWizard(String id, String title)
	{
		super(id, title);

		WizardModel wizardModel = new TaloolWizardModel();
		wizardModel.add(new MessageGiftStep());
		wizardModel.add(new MessageCriteriaStep());
		wizardModel.add(new MessageConfirmationStep());
		
		wizardModel.setLastVisible(false);

		this.init(wizardModel);
	}
	
	@Override
	public void setModelObject(MerchantGift message)
	{
		/*
		 * Considered setting the model on the form rather than the Wizard so that
		 * it would filter down to the steps directly. However, that didn't work,
		 * hence pass off via onActiveStepChanged
		 */
		this.setDefaultModel(new CompoundPropertyModel<MerchantGift>(message));
	}

	@Override
	public void onActiveStepChanged(IWizardStep step)
	{
		super.onActiveStepChanged(step);
		/*
		 * Set the model on the active set. The steps can't share the wizard's model
		 * directly. This call happens after onInitialize, and before onConfigure.
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

		try {
			Customer fromCustomer = taloolService.getCustomerForMerchant(mg.getMerchant());
			List<Customer> list = customerService.getCustomers(mg.getCriteria());
			customerService.giftToEmails(fromCustomer, list, mg.getDeal());
			LOG.debug("message sent");
		} catch (ServiceException e) {
			LOG.error("Failed to send message",e);
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
	protected void onSubmit(AjaxRequestTarget target) {
		super.onSubmit(target);
		target.add(this.getFeedbackPanel());
	}
}
