package com.talool.website;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wicket.util.lang.Bytes;

/**
 * 
 * @author clintz
 * 
 */
public final class Config extends PropertiesConfiguration
{
	private static Config instance;

	private static final String WEBSITE_MODE = "website.mode";
	private static final String UPLOAD_DIR = "upload.dir";
	private static final String UPLOAD_LOGO_MAX_SIZE_BYTES = "upload.logo.max.size.bytes";
	private static final String STATIC_LOGO_BASE_URL = "static.logo.base.url";
	private static final String IMAGE_MAGICK_PATH = "image.magick.path";
	private static final String ALLOWED_HEALTH_CHECK_IPS = "allowable.health.check.ips";

	private static final Integer DEFAULT_UPLOAD_LOGO_MAX_SIZE_BYTES = 10240;

	private Config(String file) throws ConfigurationException
	{
		super(file);
	}

	public static Config get()
	{
		return instance;
	}

	public Bytes getLogoUploadMaxBytes()
	{
		return Bytes.kilobytes(getInt(UPLOAD_LOGO_MAX_SIZE_BYTES, DEFAULT_UPLOAD_LOGO_MAX_SIZE_BYTES));
	}

	public String getStaticLogoBaseUrl()
	{
		return getString(STATIC_LOGO_BASE_URL);
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

	public String[] getAllowableHealthCheckIps()
	{
		return getStringArray(ALLOWED_HEALTH_CHECK_IPS);
	}

	public String getUploadDir()
	{
		return getString(UPLOAD_DIR);
	}

	public String getWebsiteMode()
	{
		return getString(WEBSITE_MODE, "development");
	}

	public String getImageMagickPath()
	{
		return getString(IMAGE_MAGICK_PATH);
	}

}
