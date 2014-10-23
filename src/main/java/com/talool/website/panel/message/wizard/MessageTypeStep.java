package com.talool.website.panel.message.wizard;

import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.website.component.MessageJobSelect;
import com.talool.website.panel.message.MessageJobPojo;
import com.talool.website.panel.message.MessageJobPojo.MessageJobType;

public class MessageTypeStep extends DynamicWizardStep
{

	private static final long serialVersionUID = 1L;

	public MessageTypeStep()
	{
		super(null, new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		final MessageJobPojo mg = (MessageJobPojo) getDefaultModelObject();
		
		final MessageJobSelect jobTypeSelect = new MessageJobSelect("jobType", new PropertyModel<MessageJobType>(mg,"jobType"), mg.getMerchant().getId());
		jobTypeSelect.setOutputMarkupId(true);
		addOrReplace(jobTypeSelect.setRequired(true));
		
	}

	@Override
	public boolean isLastStep()
	{
		return false;
	}

	@Override
	public IDynamicWizardStep next() {
		final MessageJobPojo mg = (MessageJobPojo) getDefaultModelObject();
		if (mg.getJobType() == MessageJobType.MerchantGiftJob)
		{
			return new MessageGiftStep(this);
		}
		else
		{
			return new MessageBookStep(this);
		}
	}

	@Override
	public IDynamicWizardStep last()
	{
		return new MessageConfirmationStep(this);
	}

}
