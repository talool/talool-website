package com.talool.website.panel.merchant.definition;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.kendo.ui.form.datetime.DateTimePicker;
import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantIdentity;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.website.component.DealTypeDropDownChoice;
import com.talool.website.component.MerchantIdentitySelect;
import com.talool.website.component.MerchantMediaWizardPanel;
import com.talool.website.component.TimeZoneDropDown;
import com.talool.website.models.AvailableMerchantsListModel;
import com.talool.website.models.DealOfferModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.CssClassToggle;
import com.talool.website.util.SessionUtils;
import com.talool.website.validators.StartEndDateFormValidator;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantDealOfferPanel extends BaseDefinitionPanel
{
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDealOfferPanel.class);
	private static final long serialVersionUID = 661849211369766802L;

	private MerchantIdentity owningMerchant;
	private MerchantMedia publisherLogo;
	private MerchantMedia publisherIcon;
	private MerchantMedia publisherBackgroundImage;
	private MerchantMediaWizardPanel mediaPanelPublisherLogo;
	private MerchantMediaWizardPanel mediaPanelPublisherIcon;
	private MerchantMediaWizardPanel mediaPanelPublisherBackgroundImage;
	
	private String selectedTimeZoneId, lastTimeZoneId;
	private DateTimePicker start, end;

	public MerchantDealOfferPanel(final String id, final MerchantIdentity merchantIdentity,
			final SubmitCallBack callback)
	{
		super(id, callback);

		Merchant merchant;
		try
		{
			this.owningMerchant = merchantIdentity;
			merchant = taloolService.getMerchantById(merchantIdentity.getId());
			final DealOffer dealOffer = domainFactory.newDealOffer(merchant, SessionUtils.getSession()
					.getMerchantAccount());

			setDefaultModel(Model.of(dealOffer));
		}
		catch (ServiceException e)
		{
			LOG.error("Problem getting merchant " + merchantIdentity);
			getSession().error("Problem getting merchant for Deal Offer create");
		}

	}

	public MerchantDealOfferPanel(final String id, final Merchant merchant,
			final SubmitCallBack callback)
	{
		super(id, callback);

		final DealOffer dealOffer = domainFactory.newDealOffer(merchant, SessionUtils.getSession().getMerchantAccount());

		setDefaultModel(Model.of(dealOffer));
	}

	public MerchantDealOfferPanel(final String id, final SubmitCallBack callback,
			final UUID dealOfferId)
	{
		super(id, callback);
		setDefaultModel(new DealOfferModel(dealOfferId));

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		MerchantIdentitySelect merchantSelect = new MerchantIdentitySelect("owningMerchant",
				new PropertyModel<MerchantIdentity>(this, "owningMerchant"),
				new AvailableMerchantsListModel());

		form.add(merchantSelect.setRequired(true));

		form.add(new DealTypeDropDownChoice("dealType").setRequired(true));
		form.add(new TextField<String>("title").setRequired(true));
		form.add(new TextField<String>("summary"));
		form.add(new TextField<String>("price"));

		DealOffer dealOffer = (DealOffer) getDefaultModelObject();

		publisherLogo = dealOffer.getDealOfferLogo();
		PropertyModel<MerchantMedia> logoModel = new PropertyModel<MerchantMedia>(this, "publisherLogo");
		mediaPanelPublisherLogo =
				new MerchantMediaWizardPanel("dealOfferLogo", dealOffer.getMerchant().getId(), MediaType.DEAL_OFFER_LOGO,
						logoModel)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media)
					{}

				};
		form.add(mediaPanelPublisherLogo);

		publisherIcon = dealOffer.getDealOfferIcon();
		PropertyModel<MerchantMedia> iconModel = new PropertyModel<MerchantMedia>(this, "publisherIcon");
		mediaPanelPublisherIcon =
				new MerchantMediaWizardPanel("dealOfferIcon", dealOffer.getMerchant().getId(), MediaType.DEAL_OFFER_MERCHANT_LOGO,
						iconModel)
				{
					private static final long serialVersionUID = 5504461189222207917L;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media)
					{}

				};
		form.add(mediaPanelPublisherIcon);

		publisherBackgroundImage = dealOffer.getDealOfferBackground();
		PropertyModel<MerchantMedia> backgroundModel = new PropertyModel<MerchantMedia>(this, "publisherBackgroundImage");
		mediaPanelPublisherBackgroundImage =
				new MerchantMediaWizardPanel("dealOfferBackground", dealOffer.getMerchant().getId(), MediaType.DEAL_OFFER_BACKGROUND_IMAGE,
						backgroundModel)
				{

					private static final long serialVersionUID = -2503252861939156978L;

					@Override
					public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media)
					{}

				};
		form.add(mediaPanelPublisherBackgroundImage);
		
		
		
		
		TimeZone bestGuessTimeZone = SessionUtils.getSession().getBestGuessTimeZone();
		selectedTimeZoneId = TimeZoneDropDown.getBestSupportedTimeZone(bestGuessTimeZone).getID();
		lastTimeZoneId = selectedTimeZoneId;
		LOG.debug("init with timezone: " + selectedTimeZoneId);

		// convert the start/end dates to merchant_account timezone, for display
		Date endDate = convertTimeZone(dealOffer.getScheduledEndDate(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getDefault());
		Date startDate = convertTimeZone(dealOffer.getScheduledStartDate(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getDefault());
		end = new DateTimePicker("scheduledEndDate", Model.of(endDate));
		start = new DateTimePicker("scheduledStartDate", Model.of(startDate));
		form.addOrReplace(end.setOutputMarkupId(true));
		form.addOrReplace(start.setOutputMarkupId(true));

		// start date must be at least today
		form.add(new StartEndDateFormValidator(start, end));

		final TimeZoneDropDown timeZoneDropDown = new TimeZoneDropDown("timeZoneSelect", new PropertyModel<String>(this, "selectedTimeZoneId"));
		timeZoneDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 8587109070528314926L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				SessionUtils.getSession().setAndPersistTimeZone(selectedTimeZoneId);

				// adjust from old TZ to new TZ
				Date endDate = convertTimeZone(end.getModelObject(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getTimeZone(lastTimeZoneId));
				Date startDate = convertTimeZone(start.getModelObject(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getTimeZone(lastTimeZoneId));
				lastTimeZoneId = selectedTimeZoneId;
				end.setModelObject(endDate);
				start.setModelObject(startDate);
				target.add(start);
				target.add(end);
			}

		});
		form.addOrReplace(timeZoneDropDown.setOutputMarkupId(true));

		final WebMarkupContainer currentTimeZone = new WebMarkupContainer("currentTimeZone");
		addOrReplace(currentTimeZone.setOutputMarkupId(true));
		currentTimeZone.add(new Label("currentTimeZoneLabel", selectedTimeZoneId));
		currentTimeZone.add(new AjaxLink<Void>("changeTimeZone")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				timeZoneDropDown.add(new CssClassToggle("hide", "show"));
				target.add(timeZoneDropDown);
				currentTimeZone.setVisible(false);
				target.add(currentTimeZone);
			}

		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<DealOffer> getDefaultCompoundPropertyModel()
	{
		return new CompoundPropertyModel<DealOffer>((IModel<DealOffer>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier()
	{
		DealOffer dealOffer = (DealOffer) form.getDefaultModelObject();
		return dealOffer.getTitle();
	}

	@Override
	public void save() throws ServiceException
	{
		final DealOffer dealOffer = (DealOffer) form.getDefaultModelObject();

		// for safety, free means free !
		if (dealOffer.getType() == DealType.FREE_BOOK || dealOffer.getType() == DealType.FREE_DEAL)
		{
			dealOffer.setPrice(0.0f);
		}

		dealOffer.setUpdatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());

		if (publisherLogo != null)
		{
			dealOffer.setDealOfferLogo(publisherLogo);
		}

		if (publisherIcon != null)
		{
			dealOffer.setDealOfferIcon(publisherIcon);
		}

		if (publisherBackgroundImage != null)
		{
			dealOffer.setDealOfferBackground(publisherBackgroundImage);
		}

		// merchant could of changed, make sure to reset it
		final Merchant merch = taloolService.getMerchantById(owningMerchant.getId());
		dealOffer.setMerchant(merch);
		taloolService.save(dealOffer);

		SessionUtils.getSession().setLastDealOffer(dealOffer);

		SessionUtils.successMessage("Successfully saved deal offer '", dealOffer.getTitle(),
				"' for merchant '", dealOffer.getMerchant().getName(), "'");
	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Deal Offer";
	}

	public MerchantIdentity getOwningMerchant()
	{
		if (owningMerchant == null)
		{
			final DealOffer dOffer = (DealOffer) getDefaultModelObject();
			owningMerchant = domainFactory.newMerchantIdentity(dOffer.getMerchant().getId(), dOffer
					.getMerchant().getName());

		}
		return owningMerchant;
	}
	
	private Date convertTimeZone(Date date, TimeZone toTimeZone, TimeZone fromTimeZone)
	{
		// get the offset
		int millisInHour = (1000 * 60 * 60);
		int fromOffset = fromTimeZone.getOffset(date.getTime()) / millisInHour;
		LOG.info("From Offset of " + fromTimeZone + " is " + fromOffset);
		int toOffset = toTimeZone.getOffset(date.getTime()) / millisInHour;
		LOG.info("To Offset of " + toTimeZone + " is " + toOffset);
		int offset = toOffset - fromOffset;

		// convert the date
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, offset);
		return c.getTime();
	}

}
