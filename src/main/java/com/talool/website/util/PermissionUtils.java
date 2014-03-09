package com.talool.website.util;

import java.util.UUID;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.website.models.DealOfferModel;
import com.talool.website.models.MerchantModel;

public class PermissionUtils {
	
	static final String superUser = "super_user";
	static final String fundraiser = "fundraiser";
	static final String publisher = "publisher";
	static final String analytics = "analytics";
	
	public static boolean isSuperUser(MerchantAccount account)
	{
		return account.getProperties().getAsBool(superUser);
	}
	
	public static boolean isPublisher(Merchant merchant)
	{
		return merchant.getProperties().getAsBool(publisher);
	}
	
	public static boolean isPublisher(UUID merchantId)
	{
		Merchant merchant = new MerchantModel(merchantId, true).getObject();
		return isPublisher(merchant);
	}
	
	public static boolean isFundraisingPublisher(Merchant merchant)
	{
		return (merchant.getProperties().getAsBool(publisher) && 
				merchant.getProperties().getAsBool(fundraiser));
	}
	
	public static boolean isFundraisingPublisher(UUID merchantId)
	{
		Merchant merchant = new MerchantModel(merchantId, true).getObject();
		return isFundraisingPublisher(merchant);
	}
	
	public static boolean canViewAnalytics(Merchant merchant)
	{
		return merchant.getProperties().getAsBool(analytics);
	}
	
	public static boolean canViewAnalytics(UUID merchantId)
	{
		Merchant merchant = new MerchantModel(merchantId, true).getObject();
		return canViewAnalytics(merchant);
	}
	
	public static boolean isFundraiser(DealOffer offer)
	{
		return offer.getProperties().getAsBool(fundraiser);
	}
	
	public static boolean isFundraiser(UUID offerId)
	{
		DealOffer offer = new DealOfferModel(offerId).getObject();
		return isFundraiser(offer);
	}
}
