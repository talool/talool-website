package com.talool.website.panel.deal.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.StaticContentStep;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.wizard.AbstractWizard;
import com.talool.core.Deal;
import com.talool.core.FactoryManager;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.pages.BasePage;
import com.talool.website.util.SessionUtils;

public class DealWizard extends AbstractWizard<Deal> {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealWizard.class);
	private boolean isEdit = false;

	public DealWizard(String id, String title)
	{
		super(id, title);
		
		final DynamicWizardModel wizardModel = new DynamicWizardModel(new DealDetails(this));
		
		// only show last button if it is an edit flow
		wizardModel.setLastVisible(isEdit);
		
		this.init(wizardModel);		
	}

	@Override
	public void setModelObject(Deal deal)
	{
		/*
		 * Considered setting the model on the form rather than the Wizard
		 * so that it would filter down to the steps directly.
		 * However, that didn't work, hence pass off via onActiveStepChanged
		 */
		this.setDefaultModel(new CompoundPropertyModel<Deal>(deal));
	}
	
	@Override
	public void onActiveStepChanged(IWizardStep step) {
		super.onActiveStepChanged(step);
		/*
		 * Set the model on the active set.
		 * The steps can't share the wizard's model directly.
		 * This call happens after onInitialize, and before onConfigure.
		 */
		((WizardStep)step).setDefaultModel(getDefaultModel());
		
		// set the visibility of the "last" button
		DynamicWizardModel wizModel = (DynamicWizardModel)((WizardStep) step).getWizardModel(); 
		Deal deal = (Deal)getDefaultModelObject();
		isEdit = (deal.getId()!=null);
		wizModel.setLastVisible(isEdit);
	}

	@Override
	protected void onFinish(AjaxRequestTarget target) 
	{
		/*
		 * Save the deal
		 */
		Deal deal = (Deal) getModelObject();
		deal.setUpdatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());
		TaloolService taloolService = FactoryManager.get().getServiceFactory().getTaloolService();
		try {
			taloolService.save(deal);
			
			StringBuilder sb = new StringBuilder("Saved Deal: ");
			this.info(sb.append(getModelObject().getTitle()).toString());
			
		} catch (ServiceException se) {
			LOG.debug("Failed to save deal: ", se);
			
			StringBuilder sb = new StringBuilder("Failed to save Deal: ");
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
		this.info("Canceled...");
		target.add(((BasePage)getPage()).feedback);
	}

	@Override
	protected void onConfigure(AjaxRequestTarget target) {
		super.onConfigure(target);
		
		WizardStep step = (WizardStep) getWizardModel().getActiveStep();
		if (step instanceof DealSave)
		{
			// the user clicked "save and finish"
			onFinish(target);
			close(target, getSubmitButton());
		}
		else 
		{
			// Show/Hide the finish button
			DialogButton finish = findButton("Finish");
			finish.setVisible(!isEdit, target);
			
			// enable/disable the "save and finish" button
			DialogButton saveAndFinish = findButton("Save & Finish");
			saveAndFinish.setEnabled(isEdit, target);
		}

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
