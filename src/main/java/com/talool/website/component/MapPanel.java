package com.talool.website.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.wicketstuff.gmap.GMap;
import org.wicketstuff.gmap.api.GIcon;
import org.wicketstuff.gmap.api.GLatLng;
import org.wicketstuff.gmap.api.GMarker;
import org.wicketstuff.gmap.api.GMarkerOptions;

import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.vividsolutions.jts.geom.Point;


public class MapPanel extends Panel 
{
	private GMap map;
	private Merchant merchant;
	private List<GLatLng> markersToShow;
	private List<GLatLng> locationsToShow = new ArrayList<GLatLng>();
	
	private static final GIcon locationIcon = new GIcon("http://maps.google.com/mapfiles/ms/icons/blue-dot.png");
	private static final GIcon redemptionIcon = new GIcon("http://www.googlemapsmarkers.com/v1/009900/");
	private static final GIcon shadowIcon = new GIcon("http://talool.com/img/000.png");

	private static final long serialVersionUID = 1L;
	
	public MapPanel(String id, Merchant merchant, List<GLatLng> pins)
	{
		super(id);
		this.merchant = merchant;
		markersToShow = pins;
		for (final MerchantLocation location : merchant.getLocations())
		{
			Point pin = (Point) location.getGeometry();
			if (pin != null)
			{
				locationsToShow.add(new GLatLng(pin.getY(), pin.getX()));
			}
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		// add the map
		map = new GMap("map");
		map.setStreetViewControlEnabled(false);
		map.setScaleControlEnabled(true);
		map.setScrollWheelZoomEnabled(true);
		addOrReplace(map);
		
		map.removeAllOverlays();
		
		for (GLatLng pin:markersToShow)
		{
			map.addOverlay(new GMarker(new GMarkerOptions(map, pin, "Redemption",redemptionIcon,shadowIcon)));
		}
		
		for (GLatLng pin:locationsToShow)
		{
			map.addOverlay(new GMarker(new GMarkerOptions(map, pin, merchant.getName(), locationIcon, shadowIcon)));
		}
		

	}
	
}
