package com.talool.website;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 
 * @author clintz
 * 
 */
public final class Config extends PropertiesConfiguration
{
	private static Config instance;

	private static final String UPLOAD_DIR = "upload.dir";

	private Config(String file) throws ConfigurationException
	{
		super(file);
	}

	public static Config get()
	{
		return instance;
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

	public String getUploadDir()
	{
		return getString(UPLOAD_DIR);
	}

}
