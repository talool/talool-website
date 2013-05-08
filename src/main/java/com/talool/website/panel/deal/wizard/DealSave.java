package com.talool.website.panel.deal.wizard;

import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;

public class DealSave extends DynamicWizardStep {

	private static final long serialVersionUID = 1L;

	public DealSave(IDynamicWizardStep previousStep) {
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
