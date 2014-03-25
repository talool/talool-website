package com.talool.website.util;

import java.util.EmptyStackException;

import org.apache.wicket.extensions.wizard.WizardModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaloolWizardModel extends WizardModel {

	private static final long serialVersionUID = 1420056891696380224L;
	private static final Logger LOG = LoggerFactory.getLogger(TaloolWizardModel.class);

	@Override
	public void next() {
		try
		{
			super.next();
		}
		catch(IllegalStateException e)
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
		catch(IllegalStateException e)
		{
			LOG.debug("no more steps", e);
		}
		catch(EmptyStackException e)
		{
			LOG.debug("no more steps", e);
		}
	}
	
	

}
