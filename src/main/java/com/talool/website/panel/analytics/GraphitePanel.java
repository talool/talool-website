package com.talool.website.panel.analytics;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.talool.website.models.MetricListModel;
import com.talool.website.models.MetricListModel.METRIC_TYPE;
import com.talool.website.models.MetricListModel.Metric;

public class GraphitePanel extends Panel {

	private static final long serialVersionUID = -2630791674862025637L;

	private String chartName;
	private MetricListModel model;
	
	public GraphitePanel(String id, String chartName, MetricListModel model)
	{
		super(id);
		this.chartName = chartName;
		this.model = model;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new Label("chartName", chartName));
		
		WebMarkupContainer img = new WebMarkupContainer("chartImage");
		add(img);
		img.add(new GraphiteBehavior(model));
		
		final ListView<Metric> metrics = new ListView<Metric>("rptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Metric> item)
			{
				Metric metric = item.getModelObject();

				final int metricTotal = metric.totalAllTime;
				
				item.add(new Label("metricName",getMetricName(metric.metricType)));
				item.add(new Label("totalAllTime",metricTotal));
				
				WebMarkupContainer metricKey = new WebMarkupContainer("metricKey");
				String color = model.getJsonColor(0, true);
				metricKey.add(new AttributeAppender("style", new Model<String>("background-color: "+color)));
				item.add(metricKey);
				
				final ListView<Metric> slices = new ListView<Metric>("slices", metric.slices)
				{
					private static final long serialVersionUID = 1L;
					private int count = 1;
					
					@Override
					protected void populateItem(ListItem<Metric> item)
					{
						Metric slice = item.getModelObject();
						item.add(new Label("sliceName",getMetricName(slice.metricType)));
						item.add(new Label("sliceTotalAllTime",slice.totalAllTime));
						
						double total = new Double(metricTotal);
						double sliceTotal = new Double(slice.totalAllTime);
						double percentage = sliceTotal/total*100.0;
						item.add(new Label("percentage",percentage));
						
						WebMarkupContainer sliceKey = new WebMarkupContainer("sliceKey");
						String color = model.getJsonColor(count++, true);
						sliceKey.add(new AttributeAppender("style", new Model<String>("background-color: "+color)));
						item.add(sliceKey);
					}
				};
				item.add(slices);

			}

		};
		add(metrics);
		
	}
	
	private String getMetricName(METRIC_TYPE mt)
	{
		String name;
		if (mt == METRIC_TYPE.ACQUIRED_DEALS_GIFTS_EMAIL_SLICE)
			name = "Gifted via Email";
		else if (mt == METRIC_TYPE.ACQUIRED_DEALS_GIFTS_FACEBOOK_SLICE)
			name = "Gifted via Facebook";
		else if (mt == METRIC_TYPE.ACQUIRED_DEALS_GIFTS_SLICE)
			name = "Gifted Deals";
		else if (mt == METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_SLICE)
			name = "Redeemed Deals";
		else if (mt == METRIC_TYPE.ACQUIRED_DEALS_TOTAL)
			name = "Acquired Deals";
		else if (mt == METRIC_TYPE.BOOKS_ACTIVATION_SLICE)
			name = "Books Activated with Code";
		else if (mt == METRIC_TYPE.BOOKS_BOOK_ACTIVATION_SLICE)
			name = "Book Name";
		else if (mt == METRIC_TYPE.BOOKS_BOOK_PURCHASE_SLICE)
			name = "Book Name";
		else if (mt == METRIC_TYPE.BOOKS_PURCHASE_SLICE)
			name = "Books Purchased in App";
		else if (mt == METRIC_TYPE.BOOKS_TOTAL)
			name = "Books";
		else if (mt == METRIC_TYPE.CUSTOMERS_ANDROID_SLICE)
			name = "Customers using Android";
		else if (mt == METRIC_TYPE.CUSTOMERS_FACEBOOK_SLICE)
			name = "Customers using Facebook";
		else if (mt == METRIC_TYPE.CUSTOMERS_IPHONE_SLICE)
			name = "Customers using iPhone";
		else if (mt == METRIC_TYPE.CUSTOMERS_LOCATION_SLICE)
			name = "Location Name";
		else if (mt == METRIC_TYPE.CUSTOMERS_TOTAL)
			name = "Customers";
		else if (mt == METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_DEAL_SLICE)
			name = "Deal";
		else if (mt == METRIC_TYPE.ACQUIRED_DEALS_REDEMPTIONS_MERCHANT_SLICE)
			name = "Merchant";
		else
			name = "";	
		
		return name;
	}
}
