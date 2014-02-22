package com.talool.website.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Label moveMessageLabel;
	private String moveMessage;
	
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

					Map<UUID,DealSummary> selectedDeals = model.getObject();
					Collection<DealSummary> deals = selectedDeals.values();
					List<UUID> dealIds = new ArrayList<UUID>();
					for (DealSummary ds : deals)
					{
						dealIds.add(ds.getDealId());
					}
					
					TaloolService taloolService = FactoryManager.get()
							.getServiceFactory().getTaloolService();
					taloolService.moveDeals(dealIds, moveToDealOffer.getId(), 
							SessionUtils.getSession().getMerchantAccount().getId());
					
					success(getMovedMessage(deals.size()));
					
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
		
		setMoveMessage();
		moveMessageLabel = new Label("moveMessage", new PropertyModel<String>(this, "moveMessage"));
		add(moveMessageLabel.setOutputMarkupId(true));
	}
	
	@SuppressWarnings("unchecked")
	private boolean isMoveEnabled()
	{
		Map<UUID,DealSummary> selectedDeals = (Map<UUID,DealSummary>)getDefaultModel().getObject();
		return (!selectedDeals.isEmpty() && moveToDealOffer!=null);
	}
	
	@SuppressWarnings("unchecked")
	private void setMoveMessage()
	{
		Map<UUID,DealSummary> selectedDeals = (Map<UUID,DealSummary>)getDefaultModel().getObject();
		StringBuilder sb = new StringBuilder("Move ");
		if (selectedDeals.isEmpty())
		{
			sb.append("selected deals to:");
		}
		else if (selectedDeals.size()==1)
		{
			sb.append("selected deal to:");
		}
		else
		{
			sb.append(selectedDeals.size()).append(" selected deals to:");
		}
		moveMessage = sb.toString();
	}
	
	private String getMovedMessage(int count)
	{
		StringBuilder sb = new StringBuilder("Your ");
		if (count==1)
		{
			sb.append("selected deal was moved to ");
		}
		else
		{
			sb.append(count).append(" selected deals were moved to ");
		}
		sb.append(moveToDealOffer.getTitle());
		return sb.toString();
	}
	
	public void reset(AjaxRequestTarget target)
	{
		mover.setEnabled(isMoveEnabled());
		target.add(mover);
		
		setMoveMessage();
		target.add(moveMessageLabel);
	}

	abstract public void onMove(AjaxRequestTarget target);

}
