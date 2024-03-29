package com.talool.website.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author clintz
 * 
 */
public class SafeSimpleDecimalFormat
{
	private final String format;
	private static final ThreadLocal<Map<String, DecimalFormat>> _dateFormats = new ThreadLocal<Map<String, DecimalFormat>>()
	{
		public Map<String, DecimalFormat> initialValue()
		{
			return new HashMap<String, DecimalFormat>();
		}
	};

	private NumberFormat getNumberFormat(String format)
	{
		Map<String, DecimalFormat> formatters = _dateFormats.get();
		DecimalFormat formatter = formatters.get(format);
		if (formatter == null)
		{
			formatter = new DecimalFormat(format);
			formatters.put(format, formatter);
		}
		return formatter;
	}

	public SafeSimpleDecimalFormat(String format)
	{
		this.format = format;
	}

	public String format(Double number)
	{
		return getNumberFormat(format).format(number);
	}

	public String format(Long number)
	{
		return getNumberFormat(format).format(number);
	}

}