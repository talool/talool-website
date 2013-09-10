package com.talool.website.pages.facebook;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.gmap.api.GLatLng;

import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.website.mobile.opengraph.MobileOpenGraphLocation;
import com.talool.website.panel.opengraph.LocationPanel;
import com.vividsolutions.jts.geom.Point;

public class OpenGraphLocation extends OpenGraphRepeator
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(OpenGraphLocation.class);

	private MerchantLocation location = null;

	public OpenGraphLocation(PageParameters parameters)
	{
		super(parameters);

		if (isMobile())
		{
			// redirect to mobile web
			throw new RestartResponseException(MobileOpenGraphLocation.class, parameters); 
		}
		
		long locId = parameters.get(0).toLong();

		try
		{
			location = taloolService.getMerchantLocationById(locId);
			setOgDescription("");
			setOgTitle(location.getMerchant().getName());
			setOgType("location"); // TODO not sure about this
			setOgImage(location.getMerchantImage().getMediaUrl());
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get gift for Facebook:", se);
		}

	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		WebMarkupContainer street = new WebMarkupContainer("street");
		street.add(new AttributeModifier("content", location.getAddress1()));
		add(street);

		WebMarkupContainer city = new WebMarkupContainer("city");
		city.add(new AttributeModifier("content", location.getCity()));
		add(city);

		WebMarkupContainer state = new WebMarkupContainer("state");
		state.add(new AttributeModifier("content", location.getStateProvinceCounty()));
		add(state);

		WebMarkupContainer zip = new WebMarkupContainer("zip");
		zip.add(new AttributeModifier("content", location.getZip()));
		add(zip);

		WebMarkupContainer country = new WebMarkupContainer("country");
		country.add(new AttributeModifier("content", location.getCountry()));
		add(country);

		WebMarkupContainer phone = new WebMarkupContainer("phone");
		phone.add(new AttributeModifier("content", location.getPhone()));
		add(phone);

		WebMarkupContainer website = new WebMarkupContainer("website");
		website.add(new AttributeModifier("content", location.getWebsiteUrl()));
		add(website);

		Point point = location.getGeometry().getCentroid();
		GLatLng center = new GLatLng(point.getY(), point.getX());

		WebMarkupContainer latitude = new WebMarkupContainer("latitude");
		latitude.add(new AttributeModifier("content", center.getLat()));
		add(latitude);

		WebMarkupContainer longitude = new WebMarkupContainer("longitude");
		longitude.add(new AttributeModifier("content", center.getLng()));
		add(longitude);

		add(new LocationPanel("object",location));
	}

	@Override
	public String getUrlPath()
	{
		StringBuilder sb = new StringBuilder("/location/");
		sb.append(location.getId());
		return sb.toString();
	}

}
