package com.talool.website.panel.deal.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.StaticContentStep;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.Form;
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
import com.talool.website.Config;
import com.talool.website.pages.BasePage;
import com.talool.website.util.SessionUtils;

public class DealWizard extends AbstractWizard<Deal> {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealWizard.class);

	public DealWizard(String id, String title)
	{
		super(id, title);
		
		WizardModel wizardModel = new WizardModel();
		wizardModel.add(new DealDetails());
		wizardModel.add(new DealTags());
		wizardModel.add(new DealAvailability());
		wizardModel.add(new SaveAndFinish());
		wizardModel.setLastVisible(true);
		
		this.init(wizardModel);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		/*
		 * Set up the form for image uploads.
		 * The form is shared for all steps, so best to do this once for all steps.
		 */
		Form form = getForm();
		form.setMultiPart(true);
		form.setMaxSize(Config.get().getLogoUploadMaxBytes());
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

		// If the user clicked "save and finish"
		if (step instanceof SaveAndFinish) {
			onFinish(target);
			close(target, getSubmitButton());
		} 
		else 
		{
			// Hide the finish button (cuz "save and finish" is all I want)
			DialogButton finish = findButton("Finish");
			finish.setVisible(false, target);
			
			// disable the save and finish button if the deal isn't fully defined
			DialogButton saveAndFinish = findButton("Save & Finish");
			saveAndFinish.setEnabled(dealReadyToSave(), target);

			
			//disable the next button if the current step is Deal Availability
			if (step instanceof DealAvailability) {
				DialogButton next = findButton(">");
				next.setEnabled(false, target);
			}
		}

	}
	
	/*
	 * I hate to duplicate the validators, but I'd like to have a quick and dirty
	 * way to eliminate errors when saving in the wizard.  Only checking the DealOffer
	 * because it's the last step and can be hard to set a default for.
	 */
	private boolean dealReadyToSave()
	{
		Deal deal = (Deal) getModelObject();
		return (deal.getDealOffer() != null);
	}

	@Override
	public int getWidth() {
		return 890;
	}
	
	// a bogus step to flag the "save and finish" button
	class SaveAndFinish extends StaticContentStep
	{
		private static final long serialVersionUID = 1L;

		public SaveAndFinish()
		{
			super("Saving", "One moment please...", Model.of(), true);
			
		}
	}
	
	
}
