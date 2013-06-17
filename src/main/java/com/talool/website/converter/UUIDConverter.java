package com.talool.website.converter;

import java.util.Locale;
import java.util.UUID;

import org.apache.wicket.util.convert.IConverter;

/**
 * 
 * @author clintz
 * 
 */
public class UUIDConverter implements IConverter<UUID>
{
	private static final long serialVersionUID = -2396374772055323535L;
	private static final UUIDConverter instance = new UUIDConverter();

	@Override
	public UUID convertToObject(String value, Locale locale)
	{
		return UUID.fromString(value);
	}

	@Override
	public String convertToString(UUID value, Locale locale)
	{
		return value.toString();
	}

	public static UUIDConverter get()
	{
		return instance;
	}

}
