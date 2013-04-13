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

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.DealModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantDealPanel extends BaseDefinitionPanel
{

	private static final long serialVersionUID = 661849211369766802L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantDealPanel.class);


	public MerchantDealPanel(final String id, final Long dealOfferId, final SubmitCallBack callback)
	{
		super(id, callback, true);
		
		DealOffer dealOffer = null;
		try {
			dealOffer = ServiceFactory.get().getTaloolService().getDealOffer(dealOfferId);
		} catch (ServiceException se) {
			LOG.error("problem loading deal offer", se);
		}

		Deal deal = domainFactory.newDeal(dealOffer);
		setDefaultModel(Model.of(deal));
	}

	public MerchantDealPanel(final String id, final SubmitCallBack callback, final Long dealId)
	{
		super(id, callback, false);;
		setDefaultModel(new DealModel(dealId));
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		form.add(new TextField<String>("title").setRequired(true));
		form.add(new TextField<String>("summary"));
		form.add(new TextField<String>("details"));
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy",false);
		form.add(new DateTextField("expires", converter).setRequired(true));
		form.add(new CheckBox("isActive"));
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<Deal> getDefaultCompoundPropertyModel() {
		return new CompoundPropertyModel<Deal>((IModel<Deal>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier() {
		Deal deal = (Deal) form.getDefaultModelObject();
		return deal.getTitle();
	}

	@Override
	public void save() throws ServiceException {
		Deal deal = (Deal) form.getDefaultModelObject();
		taloolService.save(deal);
	}

	@Override
	public String getSaveButtonLabel() {
		return "Save Deal";
	}
}
