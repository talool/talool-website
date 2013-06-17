package com.talool.website.converter;

import java.util.Locale;
import java.util.UUID;

import org.apache.wicket.util.convert.converter.AbstractConverter;

/**
 * 
 * @author clintz
 * 
 */
public class UUIDConverter extends AbstractConverter<UUID>
{
	private static final long serialVersionUID = -2396374772055323535L;
	private static final UUIDConverter instance = new UUIDConverter();

	@Override
	public UUID convertToObject(String value, Locale locale)
	{
		UUID converted = null;
		try
		{
			converted = UUID.fromString(value);
		}
		catch (Exception e)
		{
			throw newConversionException("Cannot parse '" + value + "' as UUID",
					value, locale).setResourceKey("invalid.UUID");
		}
		return converted;
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

	@Override
	protected Class<UUID> getTargetType()
	{
		return UUID.class;
	}

}
