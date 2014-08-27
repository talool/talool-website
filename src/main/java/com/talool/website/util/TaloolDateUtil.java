package com.talool.website.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TaloolDateUtil
{

	/**
	 * Get a diff between two dates
	 * 
	 * @param date1
	 *          the oldest date
	 * @param date2
	 *          the newest date
	 * @param timeUnit
	 *          the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit)
	{
		long diffInMillies = date2.getTime() - date1.getTime();
		long time = timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);

		return time;
	}

	/**
	 * Returns a string in granularity of seconds, hours or days. like:
	 * 
	 * 46 seconds OR 10 minutes OR 5 days
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static String getSimpleNiceDateDiff(final Date date1, final Date date2, final String prefixStr, final String postfixStr)
	{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		long time = getDateDiff(date1, date2, TimeUnit.SECONDS);

		StringBuilder sb = new StringBuilder();

		if (prefixStr != null)
		{
			sb.append(prefixStr);
		}

		if (time < 60)
		{
			sb.append(time).append(" seconds");
		}
		else if (time < 3600)
		{
			sb.append(Math.ceil((double) time / 60)).append(" minutes ");
		}
		else if (time <= 86400)
		{
			sb.append(Math.ceil((double) time / 3600)).append(" hours");
		}
		else
		{
			sb.append(Math.ceil((double) time / 86400)).append(" days");
		}

		if (postfixStr != null)
		{
			sb.append(postfixStr);
		}

		return sb.toString();
	}
	
	public static Date convertTimeZone(Date date, TimeZone toTimeZone, TimeZone fromTimeZone)
	{
		// get the offset
		int millisInHour = (1000 * 60 * 60);
		int fromOffset = fromTimeZone.getOffset(date.getTime()) / millisInHour;
		int toOffset = toTimeZone.getOffset(date.getTime()) / millisInHour;
		int offset = toOffset - fromOffset;

		// convert the date
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, offset);
		return c.getTime();
	}

}
