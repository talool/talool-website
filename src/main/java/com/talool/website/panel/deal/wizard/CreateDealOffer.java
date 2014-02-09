package com.talool.website.panel.deal.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.image.selection.MediaSelectionPanel;
import com.talool.website.panel.merchant.definition.MerchantDealOfferPanel;
import com.talool.website.util.SessionUtils;

public class CreateDealOffer extends DynamicWizardStep {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDealOfferPanel.class);
	private final IDynamicWizardStep nextStep;
	private final DealOffer dealOffer;
	private DealWizard wizard;
	private MerchantMedia image;
	
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

		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		addOrReplace(new DateTextField("dealOffer.expires", converter));

		addOrReplace(new CheckBox("dealOffer.isActive"));
		
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

}
