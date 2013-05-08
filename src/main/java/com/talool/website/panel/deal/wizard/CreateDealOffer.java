package com.talool.website.panel.deal.wizard;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.component.DealTypeDropDownChoice;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.merchant.definition.MerchantDealOfferPanel;
import com.talool.website.util.SessionUtils;

public class CreateDealOffer extends DynamicWizardStep {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDealOfferPanel.class);
	private final IDynamicWizardStep nextStep;
	private final DealOffer dealOffer;
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	public CreateDealOffer(IDynamicWizardStep previousStep) {
		super(previousStep, new ResourceModel("title"), new ResourceModel("summary"));
		
		this.nextStep = new DealAvailability(this);
		
		final MerchantAccount ma = SessionUtils.getSession().getMerchantAccount();
		this.dealOffer = domainFactory.newDealOffer(ma.getMerchant(), ma);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		final Deal deal = (Deal) getDefaultModelObject();
		dealOffer.setMerchant(deal.getMerchant());
		deal.setDealOffer(dealOffer);
		setDefaultModel(new CompoundPropertyModel<Deal>((IModel<Deal>) getDefaultModel()));

		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);

		addOrReplace(new DealTypeDropDownChoice("dealOffer.dealType").setRequired(true));
		addOrReplace(new TextField<String>("dealOffer.title").setRequired(true));
		addOrReplace(new TextField<String>("dealOffer.summary"));
		
		// TODO price format
		addOrReplace(new TextField<String>("dealOffer.price"));

		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		addOrReplace(new DateTextField("dealOffer.expires", converter));

		addOrReplace(new CheckBox("dealOffer.isActive"));
		
	}

	@Override
	public boolean isLastStep() {
		return false;
	}
	
	@Override
	public IDynamicWizardStep previous() {
		// TODO May need to change this if we came from DealAvailability
		return super.previous();
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
