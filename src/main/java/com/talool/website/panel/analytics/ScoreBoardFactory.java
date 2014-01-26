package com.talool.website.panel.analytics;

import java.text.NumberFormat;
import java.util.UUID;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.Constants;
import com.talool.website.service.PermissionService;
import com.talool.website.util.SafeSimpleDecimalFormat;
import com.talool.website.util.SessionUtils;

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
		TotalCustomers, TotalRedemptions, TotalEmailGifts, TotalFacebookGifts, TotalActivations, TotalFaceboolCustomers
	};

	private static class MetricCountModel extends LoadableDetachableModel<String>
	{
		private static final long serialVersionUID = -4968676121844147519L;

		private MetricType metricType;
		private UUID id;

		final String signedInEmail = SessionUtils.getSession().getMerchantAccount().getEmail();
		final UUID merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();

		public MetricCountModel(final MetricType metricType)
		{
			this.metricType = metricType;
		}

		public MetricCountModel(final MetricType metricType, UUID objId)
		{
			this.metricType = metricType;
			this.id = objId;
		}

		@Override
		protected String load()
		{
			long count = 0;

			try
			{
				switch (metricType)
				{
					case TotalCustomers:
						if (PermissionService.get().isTaloolEmail(signedInEmail))
						{
							count = ServiceFactory.get().getAnalyticService().getTotalCustomers();
						}
						else
						{
							count = ServiceFactory.get().getAnalyticService().getPublishersCustomerTotal(merchantId);
						}

						break;

					case TotalRedemptions:
						if (PermissionService.get().isTaloolEmail(signedInEmail))
						{
							count = ServiceFactory.get().getAnalyticService().getTotalRedemptions();
						}
						else
						{
							count = ServiceFactory.get().getAnalyticService().getPublishersCustomerRedemptionTotal(merchantId);
						}

						break;

					case TotalEmailGifts:
						if (PermissionService.get().isTaloolEmail(signedInEmail))
						{
							count = ServiceFactory.get().getAnalyticService().getTotalEmailGifts();
						}
						else
						{
							count = ServiceFactory.get().getAnalyticService().getPublishersEmailGiftTotal(merchantId);
						}
						break;
					case TotalFacebookGifts:
						if (PermissionService.get().isTaloolEmail(signedInEmail))
						{
							count = ServiceFactory.get().getAnalyticService().getTotalFacebookGifts();
						}
						else
						{
							count = ServiceFactory.get().getAnalyticService().getPublishersFacebookGiftTotal(merchantId);
						}
						break;
					case TotalActivations:
						count = ServiceFactory.get().getAnalyticService().getTotalActivatedCodes(this.id);
						break;
					case TotalFaceboolCustomers:
						count = ServiceFactory.get().getAnalyticService().getTotalFacebookCustomers();
						break;
				}

			}
			catch (ServiceException se)
			{
				LOG.error("Problem gettting totalCustomers", se);
			}

			final SafeSimpleDecimalFormat formatter = new SafeSimpleDecimalFormat(Constants.FORMAT_COMMA_NUMBER);

			return formatter.format(count);
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

	public static ScoreBoardPanel createTotalFacebookCustomers(String id)
	{
		return new ScoreBoardPanel(id, "Facebook Customers", new AbstractReadOnlyModel<String>()
		{

			private static final long serialVersionUID = 4759222549302823144L;

			@Override
			public String getObject()
			{
				final StringBuilder sb = new StringBuilder();
				Long fb = null;
				Long count = null;

				try
				{
					if (PermissionService.get().isTaloolEmail(SessionUtils.getSession().getMerchantAccount().getEmail()))
					{
						fb = ServiceFactory.get().getAnalyticService().getTotalFacebookCustomers();
						count = ServiceFactory.get().getAnalyticService().getTotalCustomers();
					}
					else
					{
						final UUID publisherMerchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
						fb = ServiceFactory.get().getAnalyticService().getPublishersFacebookCustomerTotal(publisherMerchantId);
						count = ServiceFactory.get().getAnalyticService().getPublishersCustomerTotal(publisherMerchantId);
					}

					final NumberFormat percentFormat = NumberFormat.getPercentInstance();
					percentFormat.setMaximumFractionDigits(2);

					final SafeSimpleDecimalFormat formatter = new SafeSimpleDecimalFormat(Constants.FORMAT_COMMA_NUMBER);

					sb.append(formatter.format(fb)).append(" (").append(String.valueOf(percentFormat.format((float) fb / (float) count))).append(")");
				}
				catch (ServiceException e)
				{
					e.printStackTrace();
				}
				return sb.toString();
			}

		});

		// <String><String>()
		// new MetricCountModel(MetricType.TotalFaceboolCustomers));

	}

	public static ScoreBoardPanel createTotalBookActivations(String id, DealOffer offer)
	{
		StringBuilder sb = new StringBuilder(offer.getTitle());
		return new ScoreBoardPanel(id, sb.append(" Activations").toString(),
				new MetricCountModel(MetricType.TotalActivations, offer.getId()));

	}
}
