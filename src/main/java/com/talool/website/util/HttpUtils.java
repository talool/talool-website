package com.talool.website.util;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 
 * @author clintz
 * 
 */
public class HttpUtils
{
	public static Point getGeometry(final String addr1, final String addr2, final String city,
			final String state) throws IOException
	{
		final String address = buildAddress(addr1, addr2, city, state);

		final Geocoder geocoder = new Geocoder();
		final GeocoderRequest geoRequest = new GeocoderRequestBuilder().setAddress(address)
				.setLanguage("en").getGeocoderRequest();

		final GeocodeResponse geoResponse = geocoder.geocode(geoRequest);

		GeocoderGeometry geometry = geoResponse.getResults().get(0).getGeometry();

		final GeometryFactory factory = new GeometryFactory(
				new PrecisionModel(PrecisionModel.FLOATING), 4326);

		final Point point = factory.createPoint(new Coordinate(geometry.getLocation().getLng()
				.doubleValue(), geometry
				.getLocation().getLat().doubleValue()));

		return point;
	}

	public static String buildAddress(final String addr1, final String addr2, final String city,
			final String state)
	{
		final StringBuilder sb = new StringBuilder();

		sb.append(addr1);
		if (StringUtils.isNotEmpty(addr2))
		{
			sb.append(" ").append(addr2);
		}

		sb.append(", ").append(city).append(", ").append(state);
		return sb.toString();
	}

}
