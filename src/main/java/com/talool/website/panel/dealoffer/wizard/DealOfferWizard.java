package com.talool.website.panel.dealoffer.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.wizard.AbstractWizard;
import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.utils.KeyValue;
import com.talool.website.pages.BasePage;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SessionUtils;
import com.talool.website.util.TaloolWizardModel;

public class DealOfferWizard extends AbstractWizard<DealOffer> {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferWizard.class);

	public DealOfferWizard(String id, String title)
	{
		super(id, title);
		
		WizardModel wizardModel = new TaloolWizardModel();
		wizardModel.add(new DealOfferDetails());
		wizardModel.add(new DealOfferBackground());
		wizardModel.add(new DealOfferIcon());
		wizardModel.add(new DealOfferLogo());
		
		wizardModel.setLastVisible(false);

		// TODO allow the user to save immediately if this is an edit flow

		this.init(wizardModel);		
	}

	@Override
	public void setModelObject(DealOffer offer)
	{
		/*
		 * Considered setting the model on the form rather than the Wizard
		 * so that it would filter down to the steps directly.
		 * However, that didn't work, hence pass off via onActiveStepChanged
		 */
		this.setDefaultModel(new CompoundPropertyModel<DealOffer>(offer));
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
		 * Save the deal
		 */
		DealOffer offer  = (DealOffer) getModelObject();

		// if this book belongs to a fundraising publisher, ensure it is set as a fundraising_book
		if (PermissionUtils.isFundraisingPublisher(offer.getMerchant()))
		{
			offer.getProperties().createOrReplace(KeyValue.fundraisingBook, true);
		}
		
		offer.setUpdatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());
		TaloolService taloolService = FactoryManager.get().getServiceFactory().getTaloolService();
		try {
			taloolService.merge(offer);
			
			StringBuilder sb = new StringBuilder("Saved Book: ");
			this.info(sb.append(getModelObject().getTitle()).toString());
			
		} catch (ServiceException se) {
			LOG.debug("Failed to save book: ", se);
			
			StringBuilder sb = new StringBuilder("Failed to save Book: ");
			this.error(sb.append(getModelObject().getTitle()).toString());
			
		} catch (Exception e)
		{
			LOG.debug("Failed to save deal: ", e);
			StringBuilder sb = new StringBuilder("Failed to save Deal: ");
			this.error(sb.append(getModelObject().getTitle()).toString());
		}
		
		target.add(((BasePage)getPage()).feedback.setEscapeModelStrings(false));
	}
	
	@Override
	protected void onCancel(AjaxRequestTarget target)
	{
		target.add(((BasePage)getPage()).feedback);
	}

	@Override
	public int getWidth() {
		return 890;
	}
	
	public void goBack(AjaxRequestTarget target)
	{
		DialogButton prev = findButton("<");
		onClick(target,prev);
	}
}
