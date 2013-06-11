package com.talool.website.pages;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.Config;

/**
 * Health check page - simply test DB query allowing only internal addresses
 * 
 * @author clintz
 * 
 */
public class HealthCheckPage extends WebPage
{
	private static final Logger LOG = LoggerFactory.getLogger(HealthCheckPage.class);

	private static final long serialVersionUID = 3520761101514499042L;

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		boolean isInternalRequest = false;

		final HttpServletRequest request = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest());

		for (String ipStartsWith : Config.get().getAllowableHealthCheckIps())
		{
			if (request.getRemoteAddr().startsWith(ipStartsWith))
			{
				isInternalRequest = true;
			}
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Health check from IP: " + request.getRemoteAddr());
		}

		if (isInternalRequest == false)
		{
			return;
		}

		try
		{
			final List<Merchant> merchants = ServiceFactory.get().getTaloolService().getMerchantByName("Talool");
			add(new Label("health", merchants.size() > 0 ? "OK" : "BAD"));
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}

	}

}
