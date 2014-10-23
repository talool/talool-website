package com.talool.website.panel.message.wizard;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.validator.GenericValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.Sex;
import com.talool.core.service.CustomerService;
import com.talool.core.service.ServiceException;
import com.talool.website.component.DealOfferSelect;
import com.talool.website.component.RangeSlider;
import com.talool.website.component.SexSelect;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.panel.message.MessageJobPojo;

public class MessageCriteriaStep extends DynamicWizardStep
{

	private static final long serialVersionUID = 1L;
	
	protected transient static final CustomerService customerService = FactoryManager.get()
			.getServiceFactory().getCustomerService();
	
	private DealOffer dealOffer;
	private Long customerCount;
	private Sex sex;
	private static final Integer[] ageRange = new Integer[] {18,100,18,100};

	public MessageCriteriaStep(IDynamicWizardStep previousStep)
	{
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();
		
		final MessageJobPojo mg = (MessageJobPojo) getDefaultModelObject();
		sex = Sex.Unknown;
		
		WebMarkupContainer descriptionPanel = new WebMarkupContainer("descriptionPanel");
		addOrReplace(descriptionPanel.setOutputMarkupId(true));
		
		// TODO limit to just a few offers (just payback?)
		DealOfferListModel listModel = new DealOfferListModel();
		initDealOfferFromModel(listModel,mg.getCriteria().getDealOfferId());
		updateCustomerCount();
		
		// show the number of customers targeted when criteria changes
		final Label count = new Label("customerCount", new PropertyModel<Integer>(this, "customerCount"));
		count.setOutputMarkupId(true);
		descriptionPanel.addOrReplace(count);

		// define the criteria
		DealOfferSelect dealOfferSelect = new DealOfferSelect("availableDealOffers", new PropertyModel<DealOffer>(this,
				"dealOffer"), listModel);
		dealOfferSelect.setRequired(true);
		descriptionPanel.add(dealOfferSelect);
		dealOfferSelect.add(new AjaxFormComponentUpdatingBehavior("change") {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				mg.getCriteria().setDealOfferId(dealOffer.getId());
				updateCustomerCount();
                target.add(count);
             } 
		});
		
		// filter by sex
		SexSelect sexSelect = new SexSelect("sex", new PropertyModel<Sex>(this,"sex"));
		sexSelect.add(new AjaxFormComponentUpdatingBehavior("change") {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				if (sex == Sex.Female || sex == Sex.Male)
				{
					mg.getCriteria().setSex(sex);
				}
				else
				{
					mg.getCriteria().setSex(null);
				}
                updateCustomerCount();
                target.add(count);
             } 
		});
		descriptionPanel.add(sexSelect);
		
		// filter by age
		RangeSlider slider = new RangeSlider("ageSlider",new PropertyModel<Integer[]>(this,"ageRange")){

			private static final long serialVersionUID = 2181967148971834642L;

			@Override
			public void onChangeComplete(AjaxRequestTarget target, Integer[] range) {
				
				// create dates based on the age range and add to the criteria
				int lowAge = range[0];
				int highAge = range[1];
				Date olderThan = getDateForInt(lowAge,ageRange[0]);
				Date youngerThan = getDateForInt(highAge,ageRange[2]);
				mg.getCriteria().setAges(olderThan, youngerThan);
				
				updateCustomerCount();
                target.add(count);
			}
			
		};
		descriptionPanel.add(slider);
		
		
		// add a validator to ensure the customerCount is greater than 0
		HiddenField<Long> hf = new HiddenField<Long>("count", new PropertyModel<Long>(this, "customerCount"));
		hf.add(new CustomerCountValidator());
		descriptionPanel.addOrReplace(hf);
		
	}
	
	private void updateCustomerCount()
	{
		// call service to get the list of customers
		try
		{
			final MessageJobPojo mg = (MessageJobPojo) getDefaultModelObject();
			customerCount = customerService.getCustomerCount(mg.getCriteria());
		}
		catch (ServiceException se)
		{
			customerCount = 0l;
		}
	}
	
	public class CustomerCountValidator implements IValidator<Long>
	{

		private static final long serialVersionUID = 333545529060229885L;

		@Override
		public void validate(IValidatable<Long> validatable)
		{

			if (!GenericValidator.minValue(validatable.getValue(), 1))
			{
				error(validatable, "invalid.criteria");
			}
		}

		private void error(IValidatable<Long> validatable, String errorKey)
		{
			ValidationError error = new ValidationError();
			error.addKey(getClass().getSimpleName() + "." + errorKey);
			validatable.error(error);
		}
	}
	
	private void initDealOfferFromModel(DealOfferListModel listModel, UUID offerId)
	{
		if (offerId==null) return;
		
		for (DealOffer o : listModel.getObject())
		{
			if (o.getId().toString().equals(offerId.toString()))
			{
				dealOffer = o;
				break;
			}
		}
	}
	
	private Date getDateForInt(int age, int limit)
	{
		if  (age==limit) return null;
		Calendar c = Calendar.getInstance();
		int currentYr = c.get(Calendar.YEAR);
		c.set(Calendar.YEAR,currentYr-age);
		return c.getTime();
	}

	@Override
	public boolean isLastStep() {
		return false;
	}

	@Override
	public IDynamicWizardStep next() {
		return new JobScheduleStep(this);
	}
	
	@Override
	public IDynamicWizardStep last()
	{
		return new MessageConfirmationStep(this);
	}

}
