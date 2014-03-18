package com.talool.website.util;

import java.util.UUID;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.utils.KeyValue;
import com.talool.website.models.DealOfferModel;
import com.talool.website.models.MerchantModel;

public class PermissionUtils {
	
	
	
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
	
	public static boolean canViewAnalytics(Merchant merchant)
	{
		return merchant.getProperties().getAsBool(KeyValue.analytics);
	}
	
	public static boolean canViewAnalytics(UUID merchantId)
	{
		Merchant merchant = new MerchantModel(merchantId, true).getObject();
		return canViewAnalytics(merchant);
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
}
