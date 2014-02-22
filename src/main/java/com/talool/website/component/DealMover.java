package com.talool.website.component;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.stats.DealSummary;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.util.SessionUtils;

public abstract class DealMover extends Panel {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(DealMover.class);
	
	private DealOffer moveToDealOffer;
	private DealOfferListModel offerListModel;
	private IndicatingAjaxLink<Void> mover;
	
	public DealMover(String id, IModel<Map<UUID,DealSummary>> model) {
		super(id, model);
		offerListModel = new DealOfferListModel();
		offerListModel.setMerchantId(SessionUtils.getSession().getMerchantAccount().getMerchant().getId());
		offerListModel.setExcludeKirke(true);
		if (offerListModel.getObject().size()==1)
		{
			moveToDealOffer = offerListModel.getObject().get(0);
		}
		setDefaultModel(model);
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		final IModel<Map<UUID,DealSummary>> model = (IModel<Map<UUID,DealSummary>>)getDefaultModel();
		mover = new IndicatingAjaxLink<Void>("move"){

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				getSession().getFeedbackMessages().clear();
				try
				{
					// TODO performance is really bad here!
					TaloolService taloolService = FactoryManager.get()
							.getServiceFactory().getTaloolService();
					
					Map<UUID,DealSummary> selectedDeals = model.getObject();
					Collection<DealSummary> deals = selectedDeals.values();
					for (DealSummary ds : deals)
					{
						Deal deal = taloolService.getDeal(ds.getDealId());
						deal.setDealOffer(moveToDealOffer);
						taloolService.merge(deal);
					}
					
					StringBuilder sb = new StringBuilder("Your selected ");
					if (deals.size()>1) sb.append("deals were ");
					else sb.append("deal was ");
					sb.append("moved.");
					success(sb.toString());
					
				}
				catch (ServiceException se)
				{
					LOG.error("Bulk Move Failed", se);
					error("We failed to move your selected deals.  Please try again.");
				}
				
				mover.setEnabled(isMoveEnabled());
				target.add(mover);
				
				onMove(target);
			}
			
		};
		add(mover.setOutputMarkupId(true).setEnabled(isMoveEnabled()));
		
		final DealOfferSelect otherBooks = new DealOfferSelect("otherBooks",new PropertyModel<DealOffer>(this,"moveToDealOffer") ,offerListModel);
		add(otherBooks.setOutputMarkupId(true));
		otherBooks.add(new AjaxFormComponentUpdatingBehavior("onchange"){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				mover.setEnabled(isMoveEnabled());
				target.add(mover);
			}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	private boolean isMoveEnabled()
	{
		Map<UUID,DealSummary> selectedDeals = (Map<UUID,DealSummary>)getDefaultModel().getObject();
		return (!selectedDeals.isEmpty() && moveToDealOffer!=null);
	}
	
	public void reset(AjaxRequestTarget target)
	{
		mover.setEnabled(isMoveEnabled());
		target.add(mover);
	}

	abstract public void onMove(AjaxRequestTarget target);

}
