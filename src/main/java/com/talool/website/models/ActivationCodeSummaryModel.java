package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.AnalyticService.ActivationCodeSummary;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.service.PermissionService;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class ActivationCodeSummaryModel extends LoadableDetachableModel<List<ActivationCodeSummary>>
{
	private static final long serialVersionUID = -2321461016744854384L;
	private static final Logger LOG = LoggerFactory.getLogger(ActivationCodeSummaryModel.class);

	@Override
	protected List<ActivationCodeSummary> load()
	{
		List<ActivationCodeSummary> summaries = null;
		try
		{
			if (PermissionService.get().isTaloolEmail(SessionUtils.getSession().getMerchantAccount().getEmail()))
			{
				// talool summaries
				summaries = ServiceFactory.get().getAnalyticService().getActivationCodeSummaries();
			}
			else
			{
				summaries = ServiceFactory.get().getAnalyticService().getPublishersActivationCodeSummaries(
						SessionUtils.getSession().getMerchantAccount().getMerchant().getId());
			}

			return summaries;
		}
		catch (ServiceException e)
		{
			LOG.error("Problem loading activationCodeSummaries: " + e.getLocalizedMessage());
		}
		return summaries;
	}

}
