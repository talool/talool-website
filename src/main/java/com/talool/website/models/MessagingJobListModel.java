package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.messaging.job.MessagingJob;
import com.talool.service.ServiceFactory;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen
 * 
 */
public class MessagingJobListModel extends
		LoadableDetachableModel<List<MessagingJob>>
{

	private static final long serialVersionUID = -6377845043575682681L;
	private static final Logger LOG = LoggerFactory.getLogger(MessagingJobListModel.class);
	private MerchantAccount ma;

	@Override
	protected List<MessagingJob> load()
	{
		List<MessagingJob> jobs = null;

		try
		{
			if (ma == null) ma = SessionUtils.getSession().getMerchantAccount();
			jobs = ServiceFactory.get().getMessagingService().getMessagingJobsByMerchantAccount(ma.getId());
			
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant locations", e);
		}

		return jobs;
	}

	public MerchantAccount getMerchantAccount() {
		return ma;
	}

	public void setMerchantAccount(MerchantAccount ma) {
		this.ma = ma;
	}

}
