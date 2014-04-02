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
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.domain.Properties;
import com.talool.utils.HttpUtils;
import com.talool.utils.KeyValue;
import com.talool.website.pages.BasePage;
import com.talool.website.util.TaloolWizardModel;

public class MerchantWizard extends AbstractWizard<Merchant>
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantWizard.class);
	public static enum WizardMarker
	{
		NewLocation
	};
	public static enum MerchantWizardMode
	{
		MERCHANT, MERCHANT_LOCATION, FUNDRAISER
	};
	private WizardStep newLocation;
	private MerchantWizardMode mode;
	
	public MerchantWizard(String id, String title, MerchantWizardMode mode)
	{
		super(id, title);
		this.mode = mode;
		this.newLocation = new MerchantLocationStep(mode);

		WizardModel wizardModel = new TaloolWizardModel();
		if (this.mode.equals(MerchantWizardMode.MERCHANT))
		{
			wizardModel.add(new MerchantDetails());
			wizardModel.add(this.newLocation);
			wizardModel.add(new MerchantLocationImage());
			wizardModel.add(new MerchantLocationLogo());
			wizardModel.add(new MerchantMap(this));
		}
		else if (this.mode.equals(MerchantWizardMode.FUNDRAISER))
		{
			wizardModel.add(new FundraiserDetails());
		}
		else
		{
			wizardModel.add(this.newLocation);
			wizardModel.add(new MerchantLocationImage());
			wizardModel.add(new MerchantLocationLogo());
		}
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
			if (mode.equals(MerchantWizardMode.MERCHANT))
			{
				if (merchant.getId()==null)
				{
					taloolService.save(merchant);
				}
				else
				{
					taloolService.merge(merchant);
				}
				StringBuilder sb = new StringBuilder("Succesfully saved merchant: ");
				this.info(sb.append(merchant.getName()).toString());
			}
			else if (mode.equals(MerchantWizardMode.FUNDRAISER))
			{
				if (merchant.getId()==null)
				{
					// set any values that can be empty...
					Properties props = merchant.getProperties();
					props.createOrReplace(KeyValue.fundraiser, "true");
					props.createOrReplace(KeyValue.percentage, 50);
					merchant.getPrimaryLocation().setEmail("");
					merchant.getPrimaryLocation().setCity("Boulder");
					merchant.getPrimaryLocation().setStateProvinceCounty("CO");
					
					taloolService.save(merchant);
					taloolService.save(merchant.getPrimaryLocation());
				}
				else
				{
					taloolService.merge(merchant);
				}
				StringBuilder sb = new StringBuilder("Succesfully saved merchant: ");
				this.info(sb.append(merchant.getName()).toString());
			}
			else
			{
				MerchantLocation loc = merchant.getCurrentLocation();
				taloolService.merge(loc);
				StringBuilder sb = new StringBuilder("Succesfully saved merchant location: ");
				this.info(sb.append(HttpUtils.buildAddress(merchant.getCurrentLocation())).toString());
			}

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

	@Override
	protected void onSubmit(AjaxRequestTarget target) {
		super.onSubmit(target);
		target.add(this.getFeedbackPanel());
	}

}
