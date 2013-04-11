package com.talool.website.validators;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.UrlValidator;

/**
 * Validates a URL or list of URLs if a separate is passed in. The separator is
 * used to split a string of URLs.
 * 
 * The Apache commons UrlValidator is wrapped and used for validation.
 * 
 * @author clintz,doug
 * 
 */
public class URLValidator extends AbstractValidator
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.wicket.validation.validator.AbstractValidator#variablesMap(org
	 * .apache.wicket.validation.IValidatable)
	 */
	@Override
	protected Map variablesMap(IValidatable validatable)
	{
		// TODO Auto-generated method stub
		return super.variablesMap(validatable);
	}
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(URLValidator.class);
	private String sepStr = null;
	private static UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
	private static URLValidator instance = new URLValidator();

	public static URLValidator getInstance()
	{
		return instance;
	}

	public URLValidator(String separator)
	{
		super();
		this.sepStr = separator;
	}

	public URLValidator()
	{
		super();
	}

	protected String resourceKey()
	{
		return INVALID_URL;
	}

	public void onValidate(IValidatable v)
	{
		String value = (String) v.getValue();
		if (sepStr == null)
		{
			if (!validate(value))
			{
				Map<String, String> m = new HashMap<String, String>(1);
				m.put("url", value);
				error(v, m);
				logger.debug(value + " is invalid");
			}
		}
		else
		{
			String[] urls = value.split(sepStr);
			for (String url : urls)
			{
				if (!validate(url))
				{
					Map<String, String> m = new HashMap<String, String>(1);
					m.put("url", url);
					error(v, m);
					logger.debug(url + " is invalid");
				}
			}
		}
	}

	public boolean validate(String value)
	{
		boolean valid = urlValidator.isValid(value);

		try
		{
			URL url = new URL(value);
		}
		catch (MalformedURLException mue)
		{
			valid = false;
		}
		catch (Exception e)
		{
			valid = false;
		}

		return valid;
	}
	private static final String INVALID_URL = "invalid.url";
}