package com.talool.website.panel.analytics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.stats.MerchantSummary;
import com.talool.website.pages.lists.MerchantSummaryDataProvider;


public class CubismHorizonFactory {
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private static final Logger LOG = LoggerFactory.getLogger(CubismHorizonFactory.class);
	
	public static List<CubismHorizon> getRedemptionMetrics(Deal deal)
	{
		List<Deal> deals = new ArrayList<Deal>();
		deals.add(deal);
		List<GraphiteMetric> list = GraphiteMetricFactory.getRedemptionMetrics(deals);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getRedemptionMetricsByOfferId(UUID id)
	{
		List<Deal> deals = new ArrayList<Deal>();
		try 
		{
			deals = taloolService.getDealsByDealOfferId(id, null, true);
		}
		catch (ServiceException se)
		{
			LOG.error("failed to get deals by deal offer id", se);
		}
		List<GraphiteMetric> list = GraphiteMetricFactory.getRedemptionMetrics(deals);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getRedemptionMetricsByMerchantId(UUID id)
	{
		List<Deal> deals = new ArrayList<Deal>();
		try 
		{
			deals = taloolService.getDealsByMerchantId(id);
		}
		catch (ServiceException se)
		{
			LOG.error("failed to get deals by merchant id", se);
		}
		List<GraphiteMetric> list = GraphiteMetricFactory.getRedemptionMetrics(deals);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getPurchaseMetrics()
	{
		List<GraphiteMetric> list = GraphiteMetricFactory.getPurchaseMetrics(null);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getPurchaseMetrics(DealOffer offer)
	{
		List<DealOffer> offers = new ArrayList<DealOffer>();
		offers.add(offer);
		List<GraphiteMetric> list = GraphiteMetricFactory.getPurchaseMetrics(offers);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getPurchaseMetricsByPublisherId(UUID id)
	{
		List<DealOffer> offers = new ArrayList<DealOffer>();
		try 
		{
			offers = taloolService.getDealOffersByMerchantId(id);
		}
		catch (ServiceException se)
		{
			LOG.error("failed to get offers by merchant id", se);
		}
		List<GraphiteMetric> list = GraphiteMetricFactory.getPurchaseMetrics(offers);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getFundraiserPurchaseMetricsByFundraiserId(UUID fundraiserId)
	{
		List<UUID> fundraisers = new ArrayList<UUID>();
		fundraisers.add(fundraiserId);
		List<GraphiteMetric> list = GraphiteMetricFactory.getFundraiserPurchaseMetrics(fundraisers);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getRegistrationMetrics()
	{
		List<GraphiteMetric> list = GraphiteMetricFactory.getRegistrationMetrics();
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getFavoriteMetrics(Merchant merchant)
	{
		List<Merchant> merchants = new ArrayList<Merchant>();
		merchants.add(merchant);
		List<GraphiteMetric> list = GraphiteMetricFactory.getFavoriteMetrics(merchants);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getGiftMetrics(Deal deal)
	{
		List<Deal> deals = new ArrayList<Deal>();
		deals.add(deal);
		List<GraphiteMetric> list = GraphiteMetricFactory.getGiftMetrics(deals);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getGiftMetricsByOfferId(UUID id)
	{
		List<Deal> deals = new ArrayList<Deal>();
		try 
		{
			deals = taloolService.getDealsByDealOfferId(id, null, true);
		}
		catch (ServiceException se)
		{
			LOG.error("failed to get deals by deal offer id", se);
		}
		List<GraphiteMetric> list = GraphiteMetricFactory.getGiftMetrics(deals);
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getAuthenticationMetrics()
	{
		return convertMetricListToHorizonList(GraphiteMetricFactory.getAuthenticationMetrics());
	}
	
	public static List<CubismHorizon> getKeyMetrics()
	{
		
		List<GraphiteMetric> list = GraphiteMetricFactory.getKeyMetrics();
		return convertMetricListToHorizonList(list);
	}
	
	public static List<CubismHorizon> getKeyMetrics(UUID publisherId)
	{
		
		List<GraphiteMetric> list = GraphiteMetricFactory.getKeyMetrics(publisherId);
		return convertMetricListToHorizonList(list);
	}
	
	private static List<CubismHorizon> convertMetricListToHorizonList(List<GraphiteMetric> list)
	{
		List<GraphiteMetric> metrics;
		List<CubismHorizon> horizons = new ArrayList<CubismHorizon>();
		for (GraphiteMetric m : list)
		{
			metrics = new ArrayList<GraphiteMetric>();
			metrics.add(m);
			horizons.add(new CubismHorizon(m.getTitle(), metrics));
		}
		
		return horizons;
	}
	
}


