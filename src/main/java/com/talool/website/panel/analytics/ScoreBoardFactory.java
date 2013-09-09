package com.talool.website.panel.analytics;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author clintz
 * 
 */
public final class ScoreBoardFactory
{
	private static final Logger LOG = LoggerFactory.getLogger(ScoreBoardFactory.class);

	private enum MetricType
	{
		TotalCustomers, TotalRedemptions, TotalEmailGifts, TotalFacebookGifts
	};

	private static class MetricCountModel extends LoadableDetachableModel<Long>
	{
		private MetricType metricType;

		public MetricCountModel(final MetricType metricType)
		{
			this.metricType = metricType;
		}

		private static final long serialVersionUID = -4968676121844147519L;

		@Override
		protected Long load()
		{
			Long count = null;
			try
			{
				switch (metricType)
				{
					case TotalCustomers:
						count = ServiceFactory.get().getAnalyticService().getTotalCustomers();
						break;

					case TotalRedemptions:
						count = ServiceFactory.get().getAnalyticService().getTotalRedemptions();
						break;

					case TotalEmailGifts:
						count = ServiceFactory.get().getAnalyticService().getTotalEmailGifts();
						break;

					case TotalFacebookGifts:
						count = ServiceFactory.get().getAnalyticService().getTotalFacebookGifts();
						break;
				}

			}
			catch (ServiceException se)
			{
				LOG.error("Pronlem gettting totalCustomers", se);
			}

			return count;
		}
	}

	public static ScoreBoardPanel createTotalCustomers(String id)
	{
		return new ScoreBoardPanel(id, "Customers",
				new MetricCountModel(MetricType.TotalCustomers));

	}

	public static ScoreBoardPanel createTotalRedemptions(String id)
	{
		return new ScoreBoardPanel(id, "Redemptions",
				new MetricCountModel(MetricType.TotalRedemptions));

	}

	public static ScoreBoardPanel createTotalEmailGifts(String id)
	{
		return new ScoreBoardPanel(id, "Email Gifts",
				new MetricCountModel(MetricType.TotalEmailGifts));

	}

	public static ScoreBoardPanel createTotalFacebookGifts(String id)
	{
		return new ScoreBoardPanel(id, "Facebook Gifts",
				new MetricCountModel(MetricType.TotalFacebookGifts));

	}
}
