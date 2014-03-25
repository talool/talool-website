package com.talool.website.util;

import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardModel;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaloolDynamicWizardModel extends DynamicWizardModel {

	private static final long serialVersionUID = 1420056891696380224L;
	private static final Logger LOG = LoggerFactory.getLogger(TaloolDynamicWizardModel.class);
	
	public TaloolDynamicWizardModel(IDynamicWizardStep startStep) {
		super(startStep);
	}

	@Override
	public void next() {
		try
		{
			super.next();
		}
		catch(IllegalArgumentException e)
		{
			LOG.debug("no more steps", e);
		}
	}

	@Override
	public void previous() {
		try
		{
			super.previous();
		}
		catch(IllegalArgumentException e)
		{
			LOG.debug("no more steps", e);
		}
	}
	
	

}
