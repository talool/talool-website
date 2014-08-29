package com.talool.website.util;

import java.util.Comparator;

import com.talool.core.MerchantAccount;

public class MerchantAccountComparator implements Comparator<MerchantAccount> {

	public enum ComparatorType
	{
		CreateDate
	};

	private ComparatorType type;

	public MerchantAccountComparator(final ComparatorType type)
	{
		this.type = type;
	}
	
	@Override
	public int compare(MerchantAccount o1, MerchantAccount o2)
	{
		int compare = 0;
		switch (type)
		{
			case CreateDate:
				compare = o1.getCreated().compareTo(o2.getCreated());
				break;
		}

		return compare;

	}
}
