package com.talool.website.panel.deal.wizard;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.wicket.kendo.ui.form.datetime.DateTimePicker;
import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.component.TimeZoneDropDown;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.image.selection.MediaSelectionPanel;
import com.talool.website.util.CssClassToggle;
import com.talool.website.util.SessionUtils;
import com.talool.website.validators.StartEndDateFormValidator;

public class CreateDealOffer extends DynamicWizardStep {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CreateDealOffer.class);
	private final IDynamicWizardStep nextStep;
	private final DealOffer dealOffer;
	private DealWizard wizard;
	private MerchantMedia image;
	
	private String selectedTimeZoneId, lastTimeZoneId;
	private DateTimePicker start, end;
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	public CreateDealOffer(IDynamicWizardStep previousStep, DealWizard wiz) {
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
		
		this.nextStep = new DealAvailability(this, wiz);
		this.wizard = wiz;
		
		final MerchantAccount ma = SessionUtils.getSession().getMerchantAccount();
		this.dealOffer = domainFactory.newDealOffer(ma.getMerchant(), ma);
		this.dealOffer.setDealType(DealType.PAID_BOOK);
		MerchantLocation offerLocation = dealOffer.getMerchant().getPrimaryLocation();
		this.dealOffer.setGeometry(offerLocation.getGeometry());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		final Deal deal = (Deal) getDefaultModelObject();
		
		deal.setDealOffer(dealOffer);
		setDefaultModel(new CompoundPropertyModel<Deal>((IModel<Deal>) getDefaultModel()));

		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);

		addOrReplace(new TextField<String>("dealOffer.title").setRequired(true));
		addOrReplace(new TextArea<String>("dealOffer.summary"));
		
		// TODO price format
		addOrReplace(new TextField<String>("dealOffer.price"));
		
		image = dealOffer.getDealOfferLogo();
		PropertyModel<MerchantMedia> selectedMediaModel = new PropertyModel<MerchantMedia>(this,"image");
		
		MediaSelectionPanel imagePanel = new MediaSelectionPanel("dealOfferLogo", dealOffer.getMerchant().getId(), MediaType.DEAL_OFFER_LOGO, selectedMediaModel)
		{

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onMediaPicked(AjaxRequestTarget target, MerchantMedia media) {
				image = media;
				dealOffer.setDealOfferLogo(media);
				deal.setDealOffer(dealOffer);
				// re-init the preview
				dealPreview.init(deal);
				target.add(dealPreview);
			}
			
		};
		imagePanel.setIFrameHeight(150);
		addOrReplace(imagePanel);
		
		TimeZone bestGuessTimeZone = SessionUtils.getSession().getBestGuessTimeZone();
		selectedTimeZoneId = TimeZoneDropDown.getBestSupportedTimeZone(bestGuessTimeZone).getID();
		lastTimeZoneId = selectedTimeZoneId;
		LOG.debug("init with timezone: " + selectedTimeZoneId);

		// convert the start/end dates to merchant_account timezone, for display
		Date endDate = convertTimeZone(dealOffer.getScheduledEndDate(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getDefault());
		Date startDate = convertTimeZone(dealOffer.getScheduledStartDate(), TimeZone.getTimeZone(selectedTimeZoneId), TimeZone.getDefault());
		end = new DateTimePicker("scheduledEndDate", Model.of(endDate));
		start = new DateTimePicker("scheduledStartDate", Model.of(startDate));
		addOrReplace(end.setOutputMarkupId(true));
		addOrReplace(start.setOutputMarkupId(true));

		// start date must be at least today
		add(new StartEndDateFormValidator(start, end));

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
		addOrReplace(timeZoneDropDown.setOutputMarkupId(true));

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

	@Override
	public boolean isLastStep() {
		return false;
	}

	@Override
	public IDynamicWizardStep next() {
		return this.nextStep;
	}
	
	@Override
	public IDynamicWizardStep last() {
		return new DealSave(this);
	}
	
	/*
	 * Save the deal offer
	 */
	@Override
	public void applyState() {
		super.applyState();
		
		final Deal deal = (Deal) getDefaultModelObject();
		final DealOffer dealOffer = deal.getDealOffer();
		
		// for safety, free means free !
		if (dealOffer.getType() == DealType.FREE_BOOK || dealOffer.getType() == DealType.FREE_DEAL)
		{
			dealOffer.setPrice(0.0f);
		}
		if (image != null)
		{
			dealOffer.setDealOfferLogo(image);
		}
		try
		{
			taloolService.save(dealOffer);
			StringBuilder sb = new StringBuilder("Saved DealOffer with id:");
			LOG.debug(sb.append(dealOffer.getId()).toString());
			SessionUtils.getSession().setLastDealOffer(dealOffer);
		}
		catch (ServiceException se)
		{
			LOG.error("failed to save new DealOffer:", se);
		}
		catch (Exception e)
		{
			LOG.error("random-ass-exception saving new DealOffer:", e);
		}
	}
	
	private Date convertTimeZone(Date date, TimeZone toTimeZone, TimeZone fromTimeZone)
	{
		if (date==null)
		{
			date = new Date();
		}
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
