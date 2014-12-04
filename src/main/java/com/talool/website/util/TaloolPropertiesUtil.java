package com.talool.website.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class TaloolPropertiesUtil {
	
	private static final Logger LOG = Logger.getLogger(TaloolPropertiesUtil.class);

	/*
	 * In Postgres 9.2 the HStore to JSON functionality is kinds shitty.
	 * It gets betting in 9.3.  So for now, we need to parse the strings we
	 * get from the DB (select %%properties) into a map.
	 */
	public static Map<String, String> getMapFromPropertiesString(String props) throws JSONException
	{
		Map<String, String> pmap = new HashMap<String,String>();

		if (!StringUtils.isEmpty(props) && props.length() > 2)
		{
			//LOG.debug(props);
			
			// {name,value,name,value}
			props = props.substring(1,props.length()-1);
			String[] p = props.split(",");
			
			for (int i=0; i < p.length; i=i+2)
			{
				pmap.put(p[i],p[i+1]);
				//LOG.debug("-- "+p[i]+":"+p[i+1]);
			}
		}
		
		return pmap;
	}
}
