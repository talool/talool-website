package com.talool.website.util;

import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.vividsolutions.jts.geom.Geometry;

public class DistanceUtils {
	
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::                                                                         :*/
	/*::  This routine calculates the distance between two points (given the     :*/
	/*::  latitude/longitude of those points). It is being used to calculate     :*/
	/*::  the distance between two locations using GeoDataSource (TM) products   :*/
	/*::                                                                         :*/
	/*::  Definitions:                                                           :*/
	/*::    South latitudes are negative, east longitudes are positive           :*/
	/*::                                                                         :*/
	/*::  Passed to function:                                                    :*/
	/*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
	/*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
	/*::    unit = the unit you desire for results                               :*/
	/*::           where: 'M' is statute miles                                   :*/
	/*::                  'K' is kilometers (default)                            :*/
	/*::                  'N' is nautical miles                                  :*/
	/*::  Worldwide cities and other features databases with latitude longitude  :*/
	/*::  are available at http://www.geodatasource.com                          :*/
	/*::                                                                         :*/
	/*::  For enquiries, please contact sales@geodatasource.com                  :*/
	/*::                                                                         :*/
	/*::  Official Web site: http://www.geodatasource.com                        :*/
	/*::                                                                         :*/
	/*::           GeoDataSource.com (C) All Rights Reserved 2014                :*/
	/*::                                                                         :*/
	/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(degToRadians(lat1)) * Math.sin(degToRadians(lat2)) + Math.cos(degToRadians(lat1)) * Math.cos(degToRadians(lat2)) * Math.cos(degToRadians(theta));
		dist = Math.acos(dist);
		dist = radiansToDegrees(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
		    dist = dist * 1.609344;
		} else if (unit == "N") {
		    dist = dist * 0.8684;
		}
		return (dist);
	}
	
	public static double distance(Geometry loc1, Geometry loc2, String unit)
	{
		return distance(loc1.getCoordinate().x, loc1.getCoordinate().y, loc2.getCoordinate().x, loc2.getCoordinate().y, unit);
	}

	public static double degToRadians(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static double radiansToDegrees(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	public static MerchantLocation getClosestLocation(Geometry geo, Merchant merchant)
	{
		double closestDistance = 1000000;
		double d = 1000000;
		MerchantLocation closestLocation = null;
		for (MerchantLocation loc:merchant.getLocations())
		{
			d = distance(geo, loc.getGeometry(), "M");
			if (d<closestDistance) 
			{
				closestDistance = d;
				closestLocation = loc;
			}
		}
		return closestLocation;
	}
}
