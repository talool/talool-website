package com.talool.website.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DevicePresence;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class DevicePresenceListModel extends LoadableDetachableModel<List<DevicePresence>>
{

	private static final long serialVersionUID = -8161469142416821672L;

	private static final Logger LOG = LoggerFactory.getLogger(DevicePresenceListModel.class);
	
	private UUID customer_id;

	public DevicePresenceListModel(final UUID customer_id)
	{
		this.customer_id = customer_id;
	}

	@Override
	protected List<DevicePresence> load()
	{

		List<DevicePresence> devices = new ArrayList<DevicePresence>();
		
		try
		{
			devices = ServiceFactory.get().getCustomerService().getDevicePresenceForCustomer(customer_id);
			Collections.sort(devices, new DevicePresenceComparator());
		}
		catch(ServiceException se)
		{
			LOG.error("failed to load devices for customer",se);
		}
		
		
		return devices;
	}
	
	public class DevicePresenceComparator implements Comparator<DevicePresence> {
	    @Override
	    public int compare(DevicePresence object1, DevicePresence object2) {
	    	String dt1 = object1.getDeviceType();
	    	String dt2 = object2.getDeviceType();
	    	if (dt1==null)dt1="";
	    	if (dt2==null)dt2="";
	        return dt1.compareToIgnoreCase(dt2);
	    }
	}
}
