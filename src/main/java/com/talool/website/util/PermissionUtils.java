package com.talool.website.util;

import java.util.UUID;

import com.talool.core.DealOffer;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.ServiceConfig;
import com.talool.utils.KeyValue;
import com.talool.website.models.DealOfferModel;
import com.talool.website.models.MerchantModel;

public class PermissionUtils {
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	
	public static boolean isSuperUser(MerchantAccount account)
	{
		return account.getProperties().getAsBool(KeyValue.superUser);
	}
	
	public static boolean isPublisher(Merchant merchant)
	{
		return merchant.getProperties().getAsBool(KeyValue.publisher);
	}
	
	public static boolean isPublisher(UUID merchantId)
	{
		Merchant merchant = new MerchantModel(merchantId, true).getObject();
		return isPublisher(merchant);
	}
	
	public static boolean isFundraisingPublisher(Merchant merchant)
	{
		return merchant.getProperties().getAsBool(KeyValue.fundraisingPublisher);
	}
	
	public static boolean isFundraisingPublisher(UUID merchantId)
	{
		Merchant merchant = new MerchantModel(merchantId, true).getObject();
		return isFundraisingPublisher(merchant);
	}
	
	public static boolean canViewAnalytics(MerchantAccount merchantAccount)
	{
		if (isSuperUser(merchantAccount)) return true;
		
		return merchantAccount.getProperties().getAsBool(KeyValue.analytics);
	}
	
	public static boolean isFundraiser(DealOffer offer)
	{
		return offer.getProperties().getAsBool(KeyValue.fundraisingBook);
	}
	
	public static boolean isFundraiser(UUID offerId)
	{
		DealOffer offer = new DealOfferModel(offerId).getObject();
		return isFundraiser(offer);
	}
	
	public static boolean canMoveDeals(MerchantAccount account)
	{
		return (account.getProperties().getAsBool(KeyValue.canMoveDeals) || isSuperUser(account));
	}
	
	/*
	 *  Kill switch for code generation.
	 *  This is meant to prevent abuse (some script filling up our DB)
	 *  and also protect us from big SendGrid bills (we send an email for each code generated).
	 */
	public static boolean isTrackingOpen(UUID publisherId)
	{
		long count = 0;
		
		// SendGrid has a limit on the number of emails it will send for free (200)
		// change the property when we change our SendGrid pricing plan 
		final long maxCodes = ServiceConfig.get().getPublisherCodeGenerationDailyQuota();
		
		try
		{
			count = taloolService.getDailyTrackingCodeCountByPublisher(publisherId);
		}
		catch (ServiceException se)
		{
			count = maxCodes;
		}
		
		return (count < maxCodes);
	}
}
