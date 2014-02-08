package com.talool.website.panel.merchant;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.wicketstuff.gmap.GMap;
import org.wicketstuff.gmap.api.GLatLng;
import org.wicketstuff.gmap.api.GMarker;
import org.wicketstuff.gmap.api.GMarkerOptions;

import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.vividsolutions.jts.geom.Point;

public class MapPreview extends Panel
{
	public MerchantLocation location;
	public String merchantName;
	public GMap map;

	private static final long serialVersionUID = 1L;

	public MapPreview(String id, Merchant merchant)
	{
		super(id);
		init(merchant);
	}

	public void init(Merchant merchant)
	{
		location = merchant.getCurrentLocation();
		merchantName = merchant.getName();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new Label("merchantName", new PropertyModel<String>(this, "merchantName")));
		
		// add the map
		map = new GMap("map");
		map.setStreetViewControlEnabled(false);
		map.setScaleControlEnabled(true);
		map.setScrollWheelZoomEnabled(true);
		map.setZoom(10);
		addOrReplace(map);
		
		Point geo = (Point)location.getGeometry();
		if (geo != null)
		{
			setLocationOnMap(geo);
		}

	}
	
	public void setLocationOnMap(Point pin)
	{
		if (pin != null)
		{
			map.removeAllOverlays();
			GLatLng center = new GLatLng(pin.getY(), pin.getX());
			map.setCenter(center);
			map.addOverlay(new GMarker(new GMarkerOptions(map, new GLatLng(pin.getY(), pin.getX()))));
		}
	}

}
