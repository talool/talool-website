package com.talool.website.panel.analytics;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.service.AnalyticService.ActivationCodeSummary;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.util.PermissionUtils;
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

	public class ActivationCodeSummaryModel extends LoadableDetachableModel<List<ActivationCodeSummary>>
	{

		private static final long serialVersionUID = 1L;

		@Override
		protected List<ActivationCodeSummary> load()
		{
			List<ActivationCodeSummary> summaries = null;
			try
			{
				summaries = ServiceFactory.get().getAnalyticService().getPublishersActivationCodeSummaries(
						SessionUtils.getSession().getMerchantAccount().getMerchant().getId());

				return summaries;
			}
			catch (ServiceException e)
			{
				LOG.error("Problem loading activationCodeSummaries: " + e.getLocalizedMessage());
			}
			return summaries;
		}

	}

	private static class MetricCountModel extends LoadableDetachableModel<MetricCount>
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
		protected MetricCount load()
		{
			Long count = 0L;
			Long total = 0L;
			try
			{
				switch (metricType)
				{
					case TotalCustomers:
						if (PermissionUtils.isSuperUser(SessionUtils.getSession().getMerchantAccount()))
						{
							count = ServiceFactory.get().getAnalyticService().getTotalCustomers();
						}
						else
						{
							count = ServiceFactory.get().getAnalyticService().getPublishersCustomerTotal(merchantId);
						}
						
						break;

					case TotalRedemptions:
						if (PermissionUtils.isSuperUser(SessionUtils.getSession().getMerchantAccount()))
						{
							count = ServiceFactory.get().getAnalyticService().getTotalRedemptions();
						}
						else
						{
							count = ServiceFactory.get().getAnalyticService().getPublishersCustomerRedemptionTotal(merchantId);
						}

						break;

					case TotalEmailGifts:
						if (PermissionUtils.isSuperUser(SessionUtils.getSession().getMerchantAccount()))
						{
							count = ServiceFactory.get().getAnalyticService().getTotalEmailGifts();
						}
						else
						{
							count = ServiceFactory.get().getAnalyticService().getPublishersEmailGiftTotal(merchantId);
						}
						break;
					case TotalFacebookGifts:
						if (PermissionUtils.isSuperUser(SessionUtils.getSession().getMerchantAccount()))
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
						if (PermissionUtils.isSuperUser(SessionUtils.getSession().getMerchantAccount()))
						{
							count = ServiceFactory.get().getAnalyticService().getTotalFacebookCustomers();
						}
						else
						{
							count = ServiceFactory.get().getAnalyticService().getPublishersFacebookCustomerTotal(merchantId);
						}
						break;
				}

			}
			catch (ServiceException se)
			{
				LOG.error("Problem gettting totalCustomers", se);
			}

			return new MetricCount(count, total);
		}
	}

	public static ScoreBoardPanel createTotalCustomers(String id)
	{
		return new ScoreBoardPanel(id, "Registrations",
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

	public static void createBookActivationPanels(final MarkupContainer container)
	{

		if (PermissionUtils.isSuperUser(SessionUtils.getSession().getMerchantAccount()))
		{

		}
		else
		{
			try
			{
				final List<ActivationCodeSummary> summaries = ServiceFactory.get().getAnalyticService().getPublishersActivationCodeSummaries(
						SessionUtils.getSession().getMerchantAccount().getMerchant().getId());

				for (final ActivationCodeSummary summary : summaries)
				{
					container.add(ScoreBoardFactory.createTotalBookActivations("totalActivations", summary));
				}
			}
			catch (ServiceException e)
			{

			}

		}
	}

	public static ScoreBoardPanel createTotalFacebookCustomers(String id)
	{
		return new ScoreBoardPanel(id, "Facebook Customers", new AbstractReadOnlyModel<MetricCount>()
		{

			private static final long serialVersionUID = 4759222549302823144L;

			@Override
			public MetricCount getObject()
			{
				Long fb = null;
				Long count = null;

				try
				{
					if (PermissionUtils.isSuperUser(SessionUtils.getSession().getMerchantAccount()))
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
				}
				catch (ServiceException e)
				{
					e.printStackTrace();
				}
				return new MetricCount(fb,count);
			}

		});

	}

	public static ScoreBoardPanel createTotalBookActivations(final String id, final ActivationCodeSummary summary)
	{
		return new ScoreBoardPanel(id, summary.dealOfferTitle, Model.of(new MetricCount(summary.totalActivatedCodes,summary.totalCodes)));
	}
}
