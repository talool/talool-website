package com.talool.website.panel.analytics;

import java.io.Serializable;
import java.text.NumberFormat;

import com.talool.website.Constants;
import com.talool.website.util.SafeSimpleDecimalFormat;

public class MetricCount implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();
	private static final SafeSimpleDecimalFormat formatter = new SafeSimpleDecimalFormat(Constants.FORMAT_COMMA_NUMBER);
	
	private long count;
	private long total;
	
	public MetricCount(long count, long total) {
		super();
		this.count = count;
		this.total = total;
		percentFormat.setMaximumFractionDigits(2);
	}
	
	public String getCount()
	{
		return formatter.format(count);
	}
	
	public String getTotal()
	{
		String p = "";
		if (total > 0)
		{
			p = formatter.format(total);
		}
		return p;
	}
	
	public String getPercentage()
	{
		String p = "";
		if (total > 0)
		{
			p = percentFormat.format((float)((float)count/(float)total));
		}
		return p;
	}
}
