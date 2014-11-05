package com.talool.rest.domain;

import com.talool.core.Merchant;
import com.talool.stats.MerchantSummary;

public class MerchantPojo {

	private String name;
	private String category;
	private String state;
	
	public MerchantPojo(Merchant merch)
	{
		name = merch.getName();
		if (merch.getCategory() != null)
		{
			category = merch.getCategory().getName();
		}
		state = merch.getPrimaryLocation().getStateProvinceCounty();
	}
	
	public MerchantPojo(MerchantSummary merch)
	{
		name = merch.getName();
		if (merch.getCategory() != null)
		{
			category = merch.getCategory();
		}
		state = merch.getState();
	}
	
	public String getName() {
		return name;
	}
	public String getState() {
		return state;
	}

	public String getCategory() {
		return category;
	}
	
	
	
}
