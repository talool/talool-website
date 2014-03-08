package com.talool.website.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.talool.domain.Properties;

public class KeyValue implements Serializable
{
	public String key;
	public String value;

	public KeyValue(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	
	public static List<KeyValue> getKeyValues(Properties props)
	{
		List<KeyValue> keyVals = new ArrayList<KeyValue>();

		for (Entry<String, String> entry : props.getAllProperties().entrySet())
		{
			keyVals.add(new KeyValue(entry.getKey(), entry.getValue()));
		}
		return keyVals;
	}

	private static final long serialVersionUID = 7882876501860467190L;

}
