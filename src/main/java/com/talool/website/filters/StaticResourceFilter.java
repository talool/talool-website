package com.talool.website.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 
 * @author clintz
 * 
 */
public class StaticResourceFilter extends OncePerRequestFilter
{

	protected final static int STATIC_CACHING_PERIOD = 365 * 24 * 60 * 60;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException
	{

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		setCachingHeaders(httpResponse, STATIC_CACHING_PERIOD);
		filterChain.doFilter(request, response);
		logger.trace("Filter processed: StaticResourceFilter.");
	}

	private void setCachingHeaders(HttpServletResponse httpResponse, int duration)
	{
		httpResponse.setHeader("Cache-Control", "public, max-age=" + duration);
		httpResponse.setDateHeader("Expires", System.currentTimeMillis() + (duration * 1000L));
		httpResponse.setDateHeader("Last-Modified", System.currentTimeMillis());
	}
}