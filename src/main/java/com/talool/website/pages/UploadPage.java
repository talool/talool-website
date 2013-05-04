package com.talool.website.pages;

import org.apache.wicket.markup.html.WebPage;
import org.wicketstuff.gmap.GMap;
import org.wicketstuff.gmap.api.GLatLng;
import org.wicketstuff.gmap.api.GMarker;
import org.wicketstuff.gmap.api.GMarkerOptions;

import com.talool.website.panel.image.upload.FileUploadPanel;
import com.talool.website.util.HttpUtils;
import com.vividsolutions.jts.geom.Point;

public class UploadPage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		FileUploadPanel fileUpload = new FileUploadPanel("fileUpload");
        add(fileUpload);
		
		GLatLng pt = new GLatLng(52.47649, 13.228573);
		
		GMap map = new GMap("map");
        map.setStreetViewControlEnabled(false);
        map.setScaleControlEnabled(true);
        map.setScrollWheelZoomEnabled(true);
        map.setCenter(pt);    
        
        map.addOverlay(new GMarker(new GMarkerOptions(map, pt)));
        
        addOrReplace(map);
	}

}
