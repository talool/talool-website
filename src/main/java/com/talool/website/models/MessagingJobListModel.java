package com.talool.website.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private List<MerchantAccount> maList = new ArrayList<MerchantAccount>();

	@Override
	protected List<MessagingJob> load()
	{
		List<MessagingJob> jobs = new ArrayList<MessagingJob>();

		try
		{
			// TODO add a service method to get messages by a merchant
			
			if (maList.isEmpty()) maList.add( SessionUtils.getSession().getMerchantAccount() );
			
			for (MerchantAccount ma:maList)
			{
				List<MessagingJob> sublist = ServiceFactory.get().getMessagingService().getMessagingJobsByMerchantAccount(ma.getId());
				if (sublist.isEmpty() == false)
				{
					jobs.addAll(sublist);
				}
			}
			
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchant locations", e);
		}

		Collections.sort(jobs, new JobComparator());
		
		return jobs;
	}

	public List<MerchantAccount> getMerchantAccounts() {
		return maList;
	}

	public void setMerchantAccounts(List<MerchantAccount> ma) {
		this.maList = ma;
	}
	
	public class JobComparator implements Comparator<MessagingJob> {
	    @Override
	    public int compare(MessagingJob object1, MessagingJob object2) {
	        return object2.getScheduledStartDate().compareTo(object1.getScheduledStartDate());
	    }
	}

}
