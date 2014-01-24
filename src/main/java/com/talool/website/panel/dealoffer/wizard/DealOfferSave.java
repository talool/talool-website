package com.talool.website.panel.dealoffer.wizard;

import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;

public class DealOfferSave extends DynamicWizardStep {

	private static final long serialVersionUID = 1L;

	public DealOfferSave(IDynamicWizardStep previousStep) {
		super(previousStep);
		// we're just going to save and exit in the deal wizard
	}

	@Override
	public boolean isLastStep() {
		return true;
	}

	@Override
	public IDynamicWizardStep next() {
		return null;
	}

}
