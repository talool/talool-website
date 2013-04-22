package com.talool.website.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import com.talool.core.AcquireStatus;

public class DealAcquireStatusListModel extends LoadableDetachableModel<List<AcquireStatus>>
{

	private static final long serialVersionUID = 1853797217232277738L;
	//private static final Logger LOG = LoggerFactory.getLogger(DealAcquireStatusListModel.class);

	@Override
	protected List<AcquireStatus> load()
	{

		List<AcquireStatus> statusList = new ArrayList<AcquireStatus>();//null;

		/*
		try
		{
			statusList = ServiceFactory.get().getTaloolService().getAcquireStatusList();
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading AcquireStatus", se);
		}
		*/

		return statusList;
	}

}
