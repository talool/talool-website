package com.talool.website.panel.merchant.definition;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.component.DealTypeDropDownChoice;
import com.talool.website.models.DealOfferModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantDealOfferPanel extends BaseDefinitionPanel
{

	private static final long serialVersionUID = 661849211369766802L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDealOfferPanel.class);

	public MerchantDealOfferPanel(final String id, final Long merchantId,
			final Long merchantAccountId, final SubmitCallBack callback)
	{
		super(id, callback, true);

		Merchant merchant = null;
		try
		{
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(merchantId);
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading merchant", se);
		}
		MerchantAccount merchantAccount = null;
		try
		{
			merchantAccount = ServiceFactory.get().getTaloolService().getMerchantAccountById(merchantId);
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading merchant account", se);
		}

		DealOffer dealOffer = domainFactory.newDealOffer(merchant, merchantAccount);
		setDefaultModel(Model.of(dealOffer));
	}

	public MerchantDealOfferPanel(final String id, final SubmitCallBack callback,
			final Long dealOfferId)
	{
		super(id, callback, false);
		setDefaultModel(new DealOfferModel(dealOfferId));
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		form.add(new DealTypeDropDownChoice("dealType").setRequired(true));
		form.add(new TextField<String>("title").setRequired(true));
		form.add(new TextField<String>("summary"));
		form.add(new TextField<String>("price").setRequired(true));
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		form.add(new DateTextField("expires", converter).setRequired(true));
		form.add(new CheckBox("isActive"));

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
		DealOffer dealOffer = (DealOffer) form.getDefaultModelObject();
		// TODO mak this a form element
		dealOffer.setDealType(DealType.PAID_BOOK);
		taloolService.save(dealOffer);
	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Deal Offer";
	}
}
