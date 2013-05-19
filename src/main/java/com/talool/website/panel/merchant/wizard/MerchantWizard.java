package com.talool.website.panel.merchant.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.wizard.AbstractWizard;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.pages.BasePage;

public class MerchantWizard extends AbstractWizard<Merchant>
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantWizard.class);
	public static enum WizardMarker
	{
		NewLocation
	};
	private WizardStep newLocation;

	public MerchantWizard(String id, String title)
	{
		super(id, title);

		this.newLocation = new MerchantLocation();

		WizardModel wizardModel = new WizardModel();
		wizardModel.add(new MerchantDetails());
		wizardModel.add(this.newLocation);
		wizardModel.add(new MerchantLocationLogo());
		wizardModel.add(new MerchantLocationImage());
		wizardModel.add(new MerchantMap(this));
		wizardModel.setLastVisible(false);

		// TODO allow the user to save immediately if this is an edit flow

		this.init(wizardModel);
	}

	@Override
	public void setModelObject(Merchant merchant)
	{
		/*
		 * Considered setting the model on the form rather than the Wizard so that
		 * it would filter down to the steps directly. However, that didn't work,
		 * hence pass off via onActiveStepChanged
		 */
		this.setDefaultModel(new CompoundPropertyModel<Merchant>(merchant));
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
		 * Save the merchant
		 */
		Merchant merchant = (Merchant) getModelObject();
		TaloolService taloolService = FactoryManager.get().getServiceFactory().getTaloolService();
		try
		{
			taloolService.merge(merchant);

			StringBuilder sb = new StringBuilder("Succesfully saved merchant: ");
			this.info(sb.append(merchant.getName()).toString());

		}
		catch (ServiceException se)
		{
			LOG.error("Failed to save merchant: ", se);

			StringBuilder sb = new StringBuilder("Failed to save merchant: ");
			this.error(sb.append(merchant.getName()).toString());

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

	public void goBack(AjaxRequestTarget target)
	{
		DialogButton prev = findButton("<");
		onClick(target, prev);
	}

	public void gotoMarker(AjaxRequestTarget target, WizardMarker marker)
	{
		WizardModel model = (WizardModel) getWizardModel();
		switch (marker)
		{
			case NewLocation:
				model.setActiveStep(this.newLocation);
				break;
		}

		// reconfigure buttons and refresh the form //
		this.onConfigure(target);

	}

}
