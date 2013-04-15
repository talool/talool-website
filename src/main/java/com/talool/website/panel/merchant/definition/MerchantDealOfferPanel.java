package com.talool.website.panel.merchant.definition;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.MerchantIdentity;
import com.talool.core.service.ServiceException;
import com.talool.website.component.DealTypeDropDownChoice;
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

	public MerchantDealOfferPanel(final String id, final MerchantIdentity merchantIdentity,
			final SubmitCallBack callback)
	{
		super(id, callback, true);

		Merchant merchant;
		try
		{
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
		DateTextField expires = new DateTextField("expires", converter);
		expires.add(new DatePicker());
		form.add(expires);

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
		dealOffer.setUpdatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());

		taloolService.save(dealOffer);
		getSession().info("Successfully created '" + dealOffer.getTitle() + "'");
	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Deal Offer";
	}
}
