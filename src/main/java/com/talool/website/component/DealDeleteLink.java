package com.talool.website.component;

import java.util.UUID;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.service.ServiceException;
import com.talool.service.ErrorCode;
import com.talool.service.ServiceFactory;
import com.talool.stats.DealSummary;
import com.talool.website.util.SessionUtils;

public abstract class DealDeleteLink extends ConfirmationIndicatingAjaxLink<Void>{

	private static final long serialVersionUID = 40399118224683921L;
	private static final Logger LOG = LoggerFactory.getLogger(DealDeleteLink.class);
	private UUID dealId;
	private String dealTitle;
	
	static String getConfirmationString(String title)
	{
		StringBuilder sb = new StringBuilder("Are you sure you want to remove \"");
		sb.append(title).append("\"?");
		return JavaScriptUtils.escapeQuotes(sb.toString()).toString();
	}

	public DealDeleteLink(String id, DealSummary deal) {
		super(id,  getConfirmationString(deal.getTitle()));
		
		dealId = deal.getDealId();
		dealTitle = deal.getTitle();
	}
	
	public DealDeleteLink(String id, Deal deal) {
		super(id,  getConfirmationString(deal.getTitle()));
		
		dealId = deal.getId();
		dealTitle = deal.getTitle();
	}
	
	
	
	@Override
	public void onClick(AjaxRequestTarget target)
	{
		boolean success = true;
		getSession().getFeedbackMessages().clear();
		try 
		{
			ServiceFactory.get().getTaloolService().deleteDealSafely(dealId, SessionUtils.getSession().getMerchantAccount().getId());
			Session.get().success(dealTitle + " has been deleted.");
		} 
		catch (ServiceException se)
		{
			ErrorCode code = se.getErrorCode();
			if (code == ErrorCode.DEAL_MOVED_NOT_DELETED)
			{
				Session.get().success(se.getMessage());
			}
			else if (code == ErrorCode.DEAL_CAN_NOT_BE_DELETED)
			{
				success = false;
				Session.get().error(se.getMessage());
			}
			else
			{
				success = false;
				LOG.error("problem deleting deal", se);
				Session.get().error("There was a problem removing this deal.");
			}
			
		}
		
		onDeleteComplete(success, target);
	}
	
	abstract public void onDeleteComplete(boolean success, AjaxRequestTarget target);


}
