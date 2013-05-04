package com.talool.website.panel.merchant.wizard;

import java.util.List;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.gmap.GMap;
import org.wicketstuff.gmap.api.GLatLng;
import org.wicketstuff.gmap.api.GMarker;
import org.wicketstuff.gmap.api.GMarkerOptions;

import com.talool.core.Address;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.website.util.HttpUtils;
import com.vividsolutions.jts.geom.Point;

public class MerchantMap extends WizardStep {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantMap.class);
	
	public MerchantMap()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
        
    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		Merchant merchant = (Merchant) getDefaultModelObject();
		
		MerchantLocation loc = merchant.getLocations().get(0);
		Address address = loc.getAddress();
		
		try {
			Point point = HttpUtils.getGeometry(address.getAddress1(), address.getAddress2(),
					address.getCity(), address.getStateProvinceCounty());
			
			GLatLng center = new GLatLng(point.getY(), point.getX());
			
			GMap map = new GMap("map");
	        map.setStreetViewControlEnabled(false);
	        map.setScaleControlEnabled(true);
	        map.setScrollWheelZoomEnabled(true);
	        map.setCenter(center);    
	        
	        List<MerchantLocation> locs = merchant.getLocations();
	        Point pin;
	        for (MerchantLocation l:locs)
	        {
	        	address = l.getAddress();
	        	pin = HttpUtils.getGeometry(address.getAddress1(), address.getAddress2(),
						address.getCity(), address.getStateProvinceCounty());
				
				map.addOverlay(new GMarker(new GMarkerOptions(map, new GLatLng(pin.getY(), pin.getX()))));
	        }
	        
	        
	        addOrReplace(map);
		}
        catch (Exception e)
		{
			LOG.error("There was an exception resolving lat/long: " + e.getLocalizedMessage(), e);
			WebMarkupContainer bug = new WebMarkupContainer("map");
			addOrReplace(bug);
		}
	}
	
}

