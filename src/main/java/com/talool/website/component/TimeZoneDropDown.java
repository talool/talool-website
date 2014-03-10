package com.talool.website.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

/**
 * 
 * An efficient Time zone drop down that supports specific time zones
 * TimeZoneDropDown.SUPPORTED_TIME_ZONES . Daylight savings is accounted for.
 * 
 * @author clintz
 * 
 */
public class TimeZoneDropDown extends DropDownChoice<String>
{
	private static final long serialVersionUID = 3901389332228767802L;

	public static String[] SUPPORTED_TIME_ZONES = { "US/Eastern", "US/Central", "US/Mountain", "US/Pacific", "US/Hawaii", "US/Alaska", "GMT" };

	private static List<String> SUPPORTED_IDS_LIST = Arrays.asList(SUPPORTED_TIME_ZONES);

	private static Map<String, String> SHORT_NAME_MAP = new HashMap<String, String>();

	static
	{
		for (String id : SUPPORTED_TIME_ZONES)
		{
			SHORT_NAME_MAP.put(TimeZone.getTimeZone(id).getDisplayName(false, TimeZone.SHORT), id);
		}
	}

	private static final TimeZoneChoiceRenderer TIME_ZONE_CHOICE_RENDERER = new TimeZoneChoiceRenderer();

	private static class TimeZoneChoiceRenderer implements IChoiceRenderer<String>
	{
		private static final long serialVersionUID = 6143588400264652991L;
		final StringBuilder sb = new StringBuilder();

		@Override
		/**
		 * Dynamic display value because we are caching supported.  Day light savings changes 2 times a year ;)
		 */
		public Object getDisplayValue(final String object)
		{
			sb.setLength(0);
			final TimeZone tz = TimeZone.getTimeZone(object);

			sb.append("(GMT ").append(DurationFormatUtils.formatDuration(tz.getOffset(System.currentTimeMillis()), "HH:mm")).append(") ").
					append(object);

			return sb.toString();

		}

		@Override
		public String getIdValue(String object, int index)
		{
			return object.toString();
		}

	}

	/**
	 * Gets the supported cached TimeZone for a given short display. If one is not
	 * mapped, the "GMT" timezone is returned
	 * 
	 * @param shortDisplay
	 * @return
	 */
	public static TimeZone getBestSupportedTimeZone(final TimeZone timeZone)
	{
		return TimeZone.getTimeZone(SHORT_NAME_MAP.get(timeZone.getDisplayName(false, TimeZone.SHORT)));
	}

	public TimeZoneDropDown(final String id, final IModel<String> selectedTimeZoneId)
	{
		super(id, selectedTimeZoneId, SUPPORTED_IDS_LIST, TIME_ZONE_CHOICE_RENDERER);

	}
}
