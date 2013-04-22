package com.talool.website.panel.customer.definition;

import java.util.UUID;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.AcquireStatus;
import com.talool.core.DealAcquire;
import com.talool.core.service.ServiceException;
import com.talool.website.models.DealAcquireModel;
import com.talool.website.models.DealAcquireStatusListModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

public class DealAcquirePanel extends BaseDefinitionPanel {

	private static final long serialVersionUID = -5353175976183753288L;
	@SuppressWarnings("unused")
	private AcquireStatus status;

	public DealAcquirePanel(String id, SubmitCallBack callback, final UUID acquireId) {
		super(id, callback);
		setDefaultModel(new DealAcquireModel(acquireId));
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		// TODO new select type
		DropDownChoice<AcquireStatus> select = new DropDownChoice<AcquireStatus>("status", new PropertyModel<AcquireStatus>(this,"status"), new DealAcquireStatusListModel() );
		//AcquireStatusSelect select = new AcquireStatusSelect("status", new PropertyModel<DealOffer>(this,"status"), new DealAcquireStatusListModel() );
		form.add(select.setRequired(true));
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<?> getDefaultCompoundPropertyModel() {
		return new CompoundPropertyModel<DealAcquire>((IModel<DealAcquire>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier() {
		final DealAcquire acquire = (DealAcquire) form.getDefaultModelObject();
		return acquire.getCustomer().getEmail();
	}

	@Override
	public void save() throws ServiceException {
		DealAcquire acquire = (DealAcquire) form.getDefaultModelObject();
		// TODO save DealAcquire in the service
		//taloolService.save(acquire);
		SessionUtils.successMessage("Successfully changed status for '", acquire.getDeal().getTitle(), "'");
	}

	@Override
	public String getSaveButtonLabel() {
		return "Purchase Deal Offer";
	}

}
