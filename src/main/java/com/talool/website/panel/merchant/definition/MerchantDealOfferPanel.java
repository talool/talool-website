package com.talool.website.panel.merchant.definition;

import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.talool.website.models.AvailableMerchantsListModel;
import com.talool.website.models.DealOfferModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

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

		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		form.add(new DateTextField("expires", converter));

		form.add(new CheckBox("isActive"));

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

}
