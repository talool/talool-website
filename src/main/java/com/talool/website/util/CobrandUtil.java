package com.talool.website.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.domain.Properties;
import com.talool.website.models.MerchantModel;

public class CobrandUtil {

	private static final String DEFAULT_MERCHANT_PARAM = "payback";
	private static final String DEFAULT_COBRAND_PARAM = "colorado";
	
	// keys used for merchant.properties and cobrand map
	private static final String KEY_COBRAND_MERCHANT = "cobrand_merchant";
	private static final String KEY_COBRAND_NAME = "cobrand_name";
	
	public static PageParameters getCobrandedPageParameters()
	{
		Merchant fundraiser = null;
		Map<String, String> map = getCobrandMap(fundraiser);
		return getCobrandedPageParameters(map);
	}
	
	public static PageParameters getCobrandedPageParameters(UUID fundraiserId)
	{
		Map<String, String> map = getCobrandMap(fundraiserId);
		return getCobrandedPageParameters(map);
	}
	
	public static PageParameters getCobrandedPageParameters(String merchantCode)
	{
		Map<String, String> map = getCobrandMap(merchantCode);
		return getCobrandedPageParameters(map);
	}
	
	private static PageParameters getCobrandedPageParameters(Map<String, String> map)
	{

		PageParameters pageParameters = new PageParameters();
		pageParameters.set("merchant",map.get(KEY_COBRAND_MERCHANT));
		pageParameters.set("cobrand",map.get(KEY_COBRAND_NAME));
		
		return pageParameters;
	}
	
	public static Map<String,String> getCobrandMap(UUID fundraiserId)
	{
		Merchant fundraiser = null;
		if (fundraiserId != null)
		{
			fundraiser = (new MerchantModel(fundraiserId, true)).getObject();
		}
		return getCobrandMap(fundraiser);
	}
	
	public static Map<String,String> getCobrandMap(String merchantCode)
	{
		Merchant fundraiser = null;
		if (merchantCode != null)
		{
			fundraiser = (new MerchantModel(merchantCode, true)).getObject();
		}
		return getCobrandMap(fundraiser);
	}
	
	private static Map<String, String> getCobrandMap(Merchant fundraiser)
	{
		Map<String, String> map = new HashMap<String, String>();
		String mParam = null;
		String cParam = null;
		
		if (fundraiser != null)
		{
			try 
			{
				Properties props = fundraiser.getProperties();
				mParam = props.getAsString(KEY_COBRAND_MERCHANT);
				cParam = props.getAsString(KEY_COBRAND_NAME);
			} catch (NullPointerException e) {}
		}
		
		if (mParam==null)
		{
			mParam = DEFAULT_MERCHANT_PARAM;
		}
		if (cParam==null)
		{
			cParam = DEFAULT_COBRAND_PARAM;
		}
		
		map.put(KEY_COBRAND_MERCHANT, mParam);
		map.put(KEY_COBRAND_NAME, cParam);
		
		return map;
	}
}
