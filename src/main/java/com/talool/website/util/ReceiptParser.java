package com.talool.website.util;

import java.util.HashMap;
import java.util.Map;

/*
 * YUCK!
 * The receipt property value should be formated as json, or some format
 * that is easier to parse.
 */

public class ReceiptParser {

	public static final String KEY_COST = "cost";
	public static final String KEY_PROCESSING_FEE = "processingFee";
	public static final String KEY_TALOOL_FEE = "taloolFee";
	public static final String KEY_FUNDRAISER_DISTRIBUTION = "fundraiserDist";
	
	/*
	"cost: 20.00 fundraiserDistPerc: 0.5 taloolFeeDiscPerc: 0.75 taloolFeePerc: 0.2 taloolFeeMin: 2.50 processingFee: 0.88 fundraiserDist: 10.00 netRev: 9.12 taloolFee: 1.51"
	*/
	public static Map<String, String> parse(String receipt)
	{
		Map<String, String> map = new HashMap<String, String>();
		String[] tokens = receipt.split(" ");
		for (int i=0; i<tokens.length; i=i+2)
		{
			String key = tokens[i].substring(0,tokens[i].length()-1);
			String val = "$"+tokens[i+1]; // format as money
			map.put(key, val);
		}
		return map;
	}
}
