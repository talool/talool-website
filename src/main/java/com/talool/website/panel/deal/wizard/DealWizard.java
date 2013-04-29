package com.talool.website.panel.deal.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.StaticContentStep;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
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

public class DealWizard extends AbstractWizard<Deal> {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealWizard.class);

	public DealWizard(String id, String title)
	{
		super(id, title);
		
		WizardModel wizardModel = new WizardModel();
		wizardModel.add(new DealDetails());
		wizardModel.add(new DealOptions());
		wizardModel.add(new SaveAndFinish());
		wizardModel.setLastVisible(true);
		
		this.init(wizardModel);
	}

	@Override
	public void setModelObject(Deal deal)
	{
		this.setDefaultModel(new CompoundPropertyModel<Deal>(deal));
	}

	@Override
	protected void onFinish(AjaxRequestTarget target) {

		Deal deal = getModelObject();
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
		
		// pass the model to the active step
		step.setDefaultModel(getDefaultModel());
		target.add(step);

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
			
			if (step instanceof DealDetails) {
				// multi-part for image uploads
				getForm().setMultiPart(true);
				getForm().setMaxSize(Config.get().getLogoUploadMaxBytes());
			}
		}

	}

	@Override
	public int getWidth() {
		return 800;
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
