package com.talool.website;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 
 * @author clintz
 * 
 */
public class Config extends PropertiesConfiguration
{
	private static Config instance;

	public Config(String file) throws ConfigurationException
	{
		super(file);
	}

	public static synchronized Configuration createInstance(final String propertyFile)
	{
		if (instance == null)
		{
			try
			{
				instance = new Config(propertyFile);
			}
			catch (ConfigurationException ex)
			{
				if (instance == null)
				{
					throw new AssertionError(ex.getLocalizedMessage());
				}
			}
		}

		return instance;
	}

}
