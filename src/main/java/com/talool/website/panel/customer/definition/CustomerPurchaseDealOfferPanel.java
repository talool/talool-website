package com.talool.website.panel.customer.definition;

import java.util.UUID;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Customer;
import com.talool.core.DealOffer;
import com.talool.core.DealOfferPurchase;
import com.talool.core.service.ServiceException;
import com.talool.website.component.DealOfferSelect;
import com.talool.website.models.CustomerModel;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

public class CustomerPurchaseDealOfferPanel extends BaseDefinitionPanel {

	private static final long serialVersionUID = -7185664671674236396L;
	private DealOffer dealOffer;

	public CustomerPurchaseDealOfferPanel(String id, SubmitCallBack callback, final UUID customerId) {
		super(id, callback);
		setDefaultModel(new CustomerModel(customerId));
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		DealOfferSelect select = new DealOfferSelect("dealOffers", new PropertyModel<DealOffer>(this,"dealOffer"), new DealOfferListModel() );
		form.add(select.setRequired(true));
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<?> getDefaultCompoundPropertyModel() {
		return new CompoundPropertyModel<Customer>((IModel<Customer>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier() {
		final Customer customer = (Customer) form.getDefaultModelObject();
		return customer.getEmail();
	}

	@Override
	public void save() throws ServiceException {
		final DealOfferPurchase purchase = domainFactory.newDealOfferPurchase((Customer) form.getDefaultModelObject(), dealOffer);
		taloolService.save(purchase);
		SessionUtils.successMessage("Successfully purchased '", purchase.getDealOffer().getTitle(), "'");
	}

	@Override
	public String getSaveButtonLabel() {
		return "Purchase Deal Offer";
	}

}
