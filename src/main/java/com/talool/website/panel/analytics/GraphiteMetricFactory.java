package com.talool.website.panel.analytics;

import java.util.ArrayList;
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
import com.talool.utils.GraphiteConstants.Action;
import com.talool.utils.GraphiteConstants.DeviceType;
import com.talool.utils.GraphiteConstants.SubAction;
import com.talool.website.util.WebsiteStatsDClient;


public class GraphiteMetricFactory {
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private static final Logger LOG = LoggerFactory.getLogger(GraphiteMetricFactory.class);
	
	public static List<GraphiteMetric> getRedemptionMetrics(List<Deal> deals)
	{
		List<UUID> ids = new ArrayList<UUID>();
		if (deals != null)
		{
			for (Deal deal : deals)
			{
				ids.add(deal.getId());
			}
		}
		
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		list.add(new GraphiteMetric("All Redemptions", null, Action.redemption, null, ids));
		list.add(new GraphiteMetric("iPhone Redemptions", DeviceType.iphone, Action.redemption, null, ids));
		list.add(new GraphiteMetric("Android Redemptions", DeviceType.android, Action.redemption, null, ids));
		return list;
	}
	
	public static List<GraphiteMetric> getPurchaseMetrics(List<DealOffer> offers)
	{
		List<UUID> ids = new ArrayList<UUID>();
		if (offers != null)
		{
			for (DealOffer offer : offers)
			{
				ids.add(offer.getId());
			}
		}
		
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		list.add(new GraphiteMetric("All Purchases", null, Action.purchase, null, ids));
		list.add(new GraphiteMetric("iPhone Purchases", DeviceType.iphone, Action.purchase, null, ids));
		list.add(new GraphiteMetric("Android Purchases", DeviceType.android, Action.purchase, null, ids));
		list.add(new GraphiteMetric("CC Purchases", null, Action.purchase, SubAction.credit_wildcard, ids));
		list.add(new GraphiteMetric("Activation Code Purchases", null, Action.purchase, SubAction.activate_code, ids));
		return list;
	}
	
	public static List<GraphiteMetric> getFundraiserPurchaseMetrics(List<UUID> fundraiserIds)
	{
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		list.add(new GraphiteMetric("Fundraiser Purchases", null, Action.fundraiser_purchase, null, fundraiserIds));
		list.add(new GraphiteMetric("iPhone Purchases", DeviceType.iphone, Action.fundraiser_purchase, null, fundraiserIds));
		list.add(new GraphiteMetric("Android Purchases", DeviceType.android, Action.fundraiser_purchase, null, fundraiserIds));
		list.add(new GraphiteMetric("CC Purchases", null, Action.fundraiser_purchase, SubAction.credit_wildcard, fundraiserIds));
		list.add(new GraphiteMetric("Tracking Codes Created", 
				WebsiteStatsDClient.Action.merchant_code_group, 
				WebsiteStatsDClient.SubAction.create, fundraiserIds));
		return list;
	}
	
	public static List<GraphiteMetric> getRegistrationMetrics()
	{
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		list.add(new GraphiteMetric("All Registrations", null, Action.registration, null, null));
		list.add(new GraphiteMetric("iPhone Registrations", DeviceType.iphone, Action.registration, null, null));
		list.add(new GraphiteMetric("Android Registrations", DeviceType.android, Action.registration, null, null));
		return list;
	}
	
	public static List<GraphiteMetric> getFavoriteMetrics(List<Merchant> merchants)
	{
		List<UUID> ids = new ArrayList<UUID>();
		if (merchants != null)
		{
			for (Merchant merchant : merchants)
			{
				ids.add(merchant.getId());
			}
		}
		
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		list.add(new GraphiteMetric("All Favorites", null, Action.favorite, null, ids));
		list.add(new GraphiteMetric("iPhone Favorites", DeviceType.iphone, Action.favorite, null, ids));
		list.add(new GraphiteMetric("Android Favorites", DeviceType.android, Action.favorite, null, ids));
		list.add(new GraphiteMetric("Add Favorites", null, Action.favorite, SubAction.add, ids));
		list.add(new GraphiteMetric("Remove Favorites", null, Action.favorite, SubAction.remove, ids));
		return list;
	}
	
	public static List<GraphiteMetric> getGiftMetrics(List<Deal> deals)
	{
		List<UUID> ids = new ArrayList<UUID>();
		if (deals != null)
		{
			for (Deal deal : deals)
			{
				ids.add(deal.getId());
			}
		}
		
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		list.add(new GraphiteMetric("All Gifts", null, Action.gift, null, ids));
		list.add(new GraphiteMetric("iPhone Gifts", DeviceType.iphone, Action.gift, null, ids));
		list.add(new GraphiteMetric("Android Gifts", DeviceType.android, Action.gift, null, ids));
		list.add(new GraphiteMetric("Email Gifts", null, Action.gift, SubAction.email, ids));
		list.add(new GraphiteMetric("Facebook Gifts", null, Action.gift, SubAction.facebook, ids));
		list.add(new GraphiteMetric("Accepted Gifts", null, Action.gift, SubAction.accept, ids));
		list.add(new GraphiteMetric("Rejected Gifts", null, Action.gift, SubAction.reject, ids));
		return list;
	}
	
	public static List<GraphiteMetric> getAuthenticationMetrics()
	{
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		list.add(new GraphiteMetric("All Authentications", null, Action.authenticate, null, null));
		list.add(new GraphiteMetric("iPhone Authentications", DeviceType.iphone, Action.authenticate, null, null));
		list.add(new GraphiteMetric("Android Authentications", DeviceType.android, Action.authenticate, null, null));
		list.add(new GraphiteMetric("User Authentications", null, Action.authenticate, SubAction.user, null));
		list.add(new GraphiteMetric("Merchant Authentications", null, Action.authenticate, SubAction.merchant, null));
		list.add(new GraphiteMetric("Request Password Reset", null, Action.password, SubAction.create_reset, null));
		return list;
	}
	
	public static List<GraphiteMetric> getAllMetrics()
	{
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		list.add(new GraphiteMetric("All Events"));
		list.addAll(getRedemptionMetrics(null));
		list.addAll(getPurchaseMetrics(null));
		list.addAll(getRegistrationMetrics());
		list.addAll(getFavoriteMetrics(null));
		list.addAll(getGiftMetrics(null));
		list.addAll(getAuthenticationMetrics());
		return list;
	}
	
	public static List<GraphiteMetric> getKeyMetrics()
	{
		return getKeyMetrics(null);
	}
	
	public static List<GraphiteMetric> getKeyMetrics(UUID publisherId)
	{	
		List<GraphiteMetric> list = new ArrayList<GraphiteMetric>();
		
		if (publisherId == null)
		{
			list.add(new GraphiteMetric("All Events"));
			list.add(new GraphiteMetric("Registrations", null, Action.registration, null, null));
			list.add(new GraphiteMetric("Purchases", null, Action.purchase, null, null));
			list.add(new GraphiteMetric("Redemptions", null, Action.redemption, null, null));
			list.add(new GraphiteMetric("Gifts", null, Action.gift, null, null));
			list.add(new GraphiteMetric("Tracking Codes Created", 
					WebsiteStatsDClient.Action.merchant_code_group, 
					WebsiteStatsDClient.SubAction.create, null));
			
			//sumSeries(stats.talool.development.apps.website.*.*.actions.merchant_code_group.create.*.users.*)
		}
		else
		{
			List<DealOffer> offers = new ArrayList<DealOffer>();
			try 
			{
				offers = taloolService.getDealOffersByMerchantId(publisherId);
			}
			catch (ServiceException se)
			{
				LOG.error("failed to get offers by merchant id", se);
			}
			list.addAll(getPurchaseMetrics(offers));
		}
		
		return list;
	}
	
}


