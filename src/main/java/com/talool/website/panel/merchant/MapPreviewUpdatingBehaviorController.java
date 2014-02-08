package com.talool.website.panel.merchant;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

import com.talool.core.FactoryManager;
import com.talool.core.MerchantLocation;
import com.talool.core.service.TaloolService;
import com.talool.utils.HttpUtils;
import com.vividsolutions.jts.geom.Point;


public class MapPreviewUpdatingBehaviorController implements Serializable {
	
	private static final long serialVersionUID = -3393286731188590664L;

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
		return (!address1.isEmpty() && !address2.isEmpty() && !city.isEmpty() && !state.isEmpty());
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
		catch (Exception e)
		{
			// TODO log and report errors
		}
		return pt;
	}

}
