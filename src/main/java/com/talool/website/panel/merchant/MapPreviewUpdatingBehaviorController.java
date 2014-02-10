package com.talool.website.panel.merchant;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.FactoryManager;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.utils.HttpUtils;
import com.vividsolutions.jts.geom.Point;


public class MapPreviewUpdatingBehaviorController implements Serializable {
	
	private static final long serialVersionUID = -3393286731188590664L;
	
	private static final Logger LOG = LoggerFactory.getLogger(MapPreviewUpdatingBehaviorController.class);

	public static enum MapComponent {ADDRESS1, ADDRESS2, CITY, STATE};
	private MapPreview preview;
	
	private String address1;
	private String address2;
	private String city;
	private String state;
	
	private String currentLocation;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public MapPreviewUpdatingBehaviorController(MapPreview preview, MerchantLocation initialLocation) {
		this.preview = preview;
		
		address1 = initialLocation.getAddress1();
		address2 = initialLocation.getAddress2();
		city = initialLocation.getCity();
		state = initialLocation.getStateProvinceCounty();
		
		currentLocation = HttpUtils.buildAddress(initialLocation);
	}
	
	public AjaxFormComponentUpdatingBehavior getBehaviorForComponent(final MapComponent component, String event)
	{
		return new AjaxFormComponentUpdatingBehavior(event){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				switch (component) {
				case ADDRESS1:
					address1 = getFormComponent().getValue();
					break;
				case ADDRESS2:
					address2 = getFormComponent().getValue();
					break;
				case CITY:
					city = getFormComponent().getValue();
					break;
				case STATE:
					state = getFormComponent().getValue();
					break;
				}
				
				if (isAddressComplete() && hasLocationChanged())
				{
					// get the new geo and update the map
					preview.setLocationOnMap(getPoint());
					target.add(preview.map);
				}
				
			}
			
		};
	}
	
	private boolean isAddressComplete()
	{
		return (!StringUtils.isEmpty(address1) && 
				!StringUtils.isEmpty(city) && 
				!StringUtils.isEmpty(state));
	}
	
	private boolean hasLocationChanged()
	{
		String location = HttpUtils.buildAddress(address1, address2, city, state);
		return (!currentLocation.equals(location));
	}
	
	private Point getPoint()
	{
		Point pt = null;
		try
		{
			// TODO discuss ways to throttle these calls.  possibly use a cache.
			pt = HttpUtils.getGeometry(address1, address2, city, state);
		}
		catch (ServiceException se)
		{
			// these errors will be reported to the user when the location step is submitted
			LOG.error("There was an exception resolving lat/long of location: " + currentLocation, se);
		}
		catch (Exception e)
		{
			LOG.error("There was an exception resolving lat/long of location: " + currentLocation, e);
		}
		return pt;
	}

}
