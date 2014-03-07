package com.talool.website.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.models.MetricListModel.Metric;

public class MetricListModel extends LoadableDetachableModel<List<Metric>>
{
	private static final long serialVersionUID = -871001031643638887L;
	private static final Logger LOG = LoggerFactory.getLogger(MetricListModel.class);
	private UUID _merchantId;
	private CHART_RANGE _range;
	private CHART_TYPE _type;
	
	public enum CHART_RANGE {LAST_6_MONTHS};
	public enum CHART_TYPE {
		CUSTOMERS, CUSTOMER_LOCATIONS, BOOKS, BOOKS_ACTIVATIONS, BOOK_PURCHASES, REDEMPTIONS, GIFTS, 
		POPULAR_MERCHANTS, POPULAR_DEALS
		};
	public enum METRIC_TYPE {
		CUSTOMERS_TOTAL, CUSTOMERS_FACEBOOK_SLICE, CUSTOMERS_IPHONE_SLICE, CUSTOMERS_ANDROID_SLICE, CUSTOMERS_LOCATION_SLICE,
		BOOKS_TOTAL, BOOKS_PURCHASE_SLICE, BOOKS_ACTIVATION_SLICE, BOOKS_BOOK_ACTIVATION_SLICE, BOOKS_BOOK_PURCHASE_SLICE,
		ACQUIRED_DEALS_TOTAL,  ACQUIRED_DEALS_GIFTS_SLICE, ACQUIRED_DEALS_GIFTS_FACEBOOK_SLICE, 
		ACQUIRED_DEALS_GIFTS_EMAIL_SLICE, ACQUIRED_DEALS_REDEMPTIONS_SLICE, ACQUIRED_DEALS_REDEMPTIONS_MERCHANT_SLICE, 
		ACQUIRED_DEALS_REDEMPTIONS_DEAL_SLICE
		};

	public MetricListModel(UUID merchantId, CHART_RANGE range, CHART_TYPE cType) 
	{
		super();
		_merchantId = merchantId;
		_range = range;
		_type = cType;
	}
	
	@Override
	protected List<Metric> load()
	{
		List<Metric> metrics = new ArrayList<Metric>();
		
		if (_type == CHART_TYPE.CUSTOMERS)
		{
			Metric m = new Metric(METRIC_TYPE.CUSTOMERS_TOTAL, 2000);
			m.slices.add(new Metric(METRIC_TYPE.CUSTOMERS_FACEBOOK_SLICE, 200));
			m.slices.add(new Metric(METRIC_TYPE.CUSTOMERS_IPHONE_SLICE, 1000));
			m.slices.add(new Metric(METRIC_TYPE.CUSTOMERS_ANDROID_SLICE, 1000));
			metrics.add(m);
		}
		else if (_type == CHART_TYPE.CUSTOMER_LOCATIONS)
		{
			// TODO How do we rollup of the location metrics?  How do we passing in a location summary object?
			Metric m = new Metric(METRIC_TYPE.CUSTOMERS_TOTAL, 2000);
			m.slices.add(new Metric(METRIC_TYPE.CUSTOMERS_LOCATION_SLICE, 1000));
			m.slices.add(new Metric(METRIC_TYPE.CUSTOMERS_LOCATION_SLICE, 400));
			m.slices.add(new Metric(METRIC_TYPE.CUSTOMERS_LOCATION_SLICE, 200));
			metrics.add(m);
		}
		else if (_type == CHART_TYPE.BOOKS)
		{
			Metric m = new Metric(METRIC_TYPE.BOOKS_TOTAL, 2000);
			m.slices.add(new Metric(METRIC_TYPE.BOOKS_PURCHASE_SLICE, 1200));
			m.slices.add(new Metric(METRIC_TYPE.BOOKS_ACTIVATION_SLICE, 800));
			metrics.add(m);
		}
		else if (_type == CHART_TYPE.BOOKS_ACTIVATIONS)
		{
			// TODO How do we passing in a location summary object?
			Metric m = new Metric(METRIC_TYPE.BOOKS_TOTAL, 2000);
			m.slices.add(new Metric(METRIC_TYPE.BOOKS_BOOK_ACTIVATION_SLICE, 1300));
			m.slices.add(new Metric(METRIC_TYPE.BOOKS_BOOK_ACTIVATION_SLICE, 500));
			m.slices.add(new Metric(METRIC_TYPE.BOOKS_BOOK_ACTIVATION_SLICE, 200));
			metrics.add(m);
		}
		else if (_type == CHART_TYPE.BOOK_PURCHASES)
		{
			Metric m = new Metric(METRIC_TYPE.BOOKS_TOTAL, 2000);
			m.slices.add(new Metric(METRIC_TYPE.BOOKS_BOOK_PURCHASE_SLICE, 600));
			m.slices.add(new Metric(METRIC_TYPE.BOOKS_BOOK_PURCHASE_SLICE, 400));
			m.slices.add(new Metric(METRIC_TYPE.BOOKS_BOOK_PURCHASE_SLICE, 300));
			metrics.add(m);
		}
		else if (_type == CHART_TYPE.REDEMPTIONS)
		{
			Metric m = new Metric(METRIC_TYPE.ACQUIRED_DEALS_TOTAL, 2000);
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_SLICE, 400));
			metrics.add(m);
		}
		else if (_type == CHART_TYPE.GIFTS)
		{
			Metric m = new Metric(METRIC_TYPE.ACQUIRED_DEALS_TOTAL, 2000);
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_GIFTS_SLICE, 800));
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_GIFTS_FACEBOOK_SLICE, 500));
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_GIFTS_EMAIL_SLICE, 300));
			metrics.add(m);
		}
		else if (_type == CHART_TYPE.POPULAR_DEALS)
		{
			Metric m = new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_SLICE, 400);
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_DEAL_SLICE, 80));
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_DEAL_SLICE, 50));
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_DEAL_SLICE, 30));
			metrics.add(m);
		}
		else if (_type == CHART_TYPE.POPULAR_MERCHANTS)
		{
			Metric m = new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_SLICE, 400);
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_MERCHANT_SLICE, 80));
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_MERCHANT_SLICE, 50));
			m.slices.add(new Metric(METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_MERCHANT_SLICE, 30));
			metrics.add(m);
		}

		return metrics;
	}

	public void setMerchantId(final UUID id)
	{
		_merchantId = id;
	}
	
	public void setRange(CHART_RANGE range)
	{
		_range = range;
		//_labels = Arrays.asList(new String[]{"August", "September", "October", "November", "December", "January"});
	}
	
	public CHART_RANGE getChartRange()
	{
		return _range;
	}
	
	public void setChartType(CHART_TYPE type)
	{
		_type = type;
	}
	
	public CHART_TYPE getChartType()
	{
		return _type;
	}
	
	public class Metric implements Serializable
	{

		private static final long serialVersionUID = 1L;
		public METRIC_TYPE metricType;
		public int totalAllTime;
		public List<Metric> slices;
		public List<Integer> data;
		
		public Metric(METRIC_TYPE metricType, int total) {
			super();
			this.metricType = metricType;
			this.totalAllTime = total;
			
			// data points for a given segment (range)
			data = new ArrayList<Integer>();
			for (int i=0; i<6; i++)
			{
				data.add(getDataPoint(total/5));
			}
			
			slices = new ArrayList<Metric>();
		}
		
		private Integer getDataPoint(double max)
		{
			
			Number rdm = Math.random()*max;
			return rdm.intValue();
		}
		
	}
	
	private final static String[] darkColors = new String[]{
    	"rgba(220,220,220,1)",
    	"rgba(25, 188, 185, 0.7)",
    	"rgba(241, 90, 36, 0.7)",
    	"rgba(80, 185, 72, 0.7)",
    	"rgba(237, 28, 36, 0.6)"
    };
    private final static String[] lightColors = new String[]{
    	"rgba(220,220,220,0.5)",
    	"rgba(25, 188, 185, 0.4)",
    	"rgba(241, 90, 36, 0.4)",
    	"rgba(80, 185, 72, 0.4)",
    	"rgba(237, 28, 36, 0.3)"
    };
    public String getJsonColor(int index, boolean isDark)
    {
    	if (index<darkColors.length)
    	{
    		return (isDark)?darkColors[index]:lightColors[index];
    	}
    	return "rgba(220,220,220,1)";
    	
    	
    }

}
