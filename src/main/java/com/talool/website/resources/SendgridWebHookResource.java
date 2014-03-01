package com.talool.website.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.request.resource.AbstractResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author clintz
 * 
 */
public class SendgridWebHookResource extends AbstractResource
{
	private static final long serialVersionUID = 1322402149481420123L;
	private static final Logger LOG = LoggerFactory.getLogger(SendgridWebHookResource.class);

	public static class SendgridEvent
	{
		public String event;
		public String email;
		public String status;
		public String reason;
		public Long timestamp;
		public String[] category;

	}

	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes)
	{
		final ResourceResponse resourceResponse = new ResourceResponse();
		final HttpServletRequest request = (HttpServletRequest) attributes.getRequest().getContainerRequest();
		String json = null;
		resourceResponse.setContentType("text/html");
		resourceResponse.setTextEncoding("utf-8");

		Enumeration enumeraton = request.getHeaderNames();
		while (enumeraton.hasMoreElements())
		{
			String name = (String) enumeraton.nextElement();
			LOG.debug(String.format("headerName: %s headerVal:%s", name, request.getHeader(name)));
		}

		final String remoteHost = request.getHeader("X-Forwarded-For");
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Remote host: " + remoteHost);
		}

		try
		{
			json = IOUtils.toString(request.getInputStream());

			SendgridWebHookResource.parseJson(json);

			resourceResponse.setStatusCode(200);

		}
		catch (IOException e)
		{
			LOG.error("Problem getting json :" + e.getLocalizedMessage(), e);
			resourceResponse.setStatusCode(500);
		}

		resourceResponse.setWriteCallback(new WriteCallback()
		{
			@Override
			public void writeData(Attributes attributes) throws IOException
			{
				OutputStream outputStream = attributes.getResponse().getOutputStream();
				Writer writer = new OutputStreamWriter(outputStream);
				writer.write("");
				writer.close();
			}
		});

		return resourceResponse;

	}

	public static void parseJson(final String json)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		SendgridEvent[] events = gson.fromJson(json, SendgridEvent[].class);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(gson.toJson(events));
		}

	}

	public static void main(String args[])
	{
		String json = "[\r\n    {\r\n        \"email\": \"john.doe@sendgrid.com\",\r\n        \"sg_event_id\": \"VzcPxPv7SdWvUugt-xKymw\",\r\n        \"sg_message_id\": \"142d9f3f351.7618.254f56.filter-147.22649.52A663508.0\",\r\n        \"timestamp\": 1386636112,\r\n        \"smtp-id\": \"<142d9f3f351.7618.254f56@sendgrid.com>\",\r\n        \"event\": \"processed\",\r\n        \"category\": [\r\n            \"category1\",\r\n            \"category2\",\r\n            \"category3\"\r\n        ],\r\n        \"id\": \"001\",\r\n        \"purchase\": \"PO1452297845\",\r\n        \"uid\": \"123456\"\r\n    },\r\n    {\r\n        \"email\": \"not an email address\",\r\n        \"smtp-id\": \"<4FB29F5D.5080404@sendgrid.com>\",\r\n        \"timestamp\": 1386636115,\r\n        \"reason\": \"Invalid\",\r\n        \"event\": \"dropped\",\r\n        \"category\": [\r\n            \"category1\",\r\n            \"category2\",\r\n            \"category3\"\r\n        ],\r\n        \"id\": \"001\",\r\n        \"purchase\": \"PO1452297845\",\r\n        \"uid\": \"123456\"\r\n    },\r\n    {\r\n        \"email\": \"john.doe@sendgrid.com\",\r\n        \"sg_event_id\": \"vZL1Dhx34srS-HkO-gTXBLg\",\r\n        \"sg_message_id\": \"142d9f3f351.7618.254f56.filter-147.22649.52A663508.0\",\r\n        \"timestamp\": 1386636113,\r\n        \"smtp-id\": \"<142d9f3f351.7618.254f56@sendgrid.com>\",\r\n        \"event\": \"delivered\",\r\n        \"category\": [\r\n            \"category1\",\r\n            \"category2\",\r\n            \"category3\"\r\n        ],\r\n        \"id\": \"001\",\r\n        \"purchase\": \"PO1452297845\",\r\n        \"uid\": \"123456\"\r\n    },\r\n    {\r\n        \"email\": \"john.smith@sendgrid.com\",\r\n        \"timestamp\": 1386636127,\r\n        \"uid\": \"123456\",\r\n        \"ip\": \"174.127.33.234\",\r\n        \"purchase\": \"PO1452297845\",\r\n        \"useragent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36\",\r\n        \"id\": \"001\",\r\n        \"category\": [\r\n            \"category1\",\r\n            \"category2\",\r\n            \"category3\"\r\n        ],\r\n        \"event\": \"open\"\r\n    },\r\n    {\r\n        \"uid\": \"123456\",\r\n        \"ip\": \"174.56.33.234\",\r\n        \"purchase\": \"PO1452297845\",\r\n        \"useragent\": \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36\",\r\n        \"event\": \"click\",\r\n        \"email\": \"john.doe@sendgrid.com\",\r\n        \"timestamp\": 1386637216,\r\n        \"url\": \"http://www.google.com/\",\r\n        \"category\": [\r\n            \"category1\",\r\n            \"category2\",\r\n            \"category3\"\r\n        ],\r\n        \"id\": \"001\"\r\n    },\r\n    {\r\n        \"uid\": \"123456\",\r\n        \"status\": \"5.1.1\",\r\n        \"sg_event_id\": \"X_C_clhwSIi4EStEpol-SQ\",\r\n        \"reason\": \"550 5.1.1 The email account that you tried to reach does not exist. Please try double-checking the recipient's email address for typos or unnecessary spaces. Learn more at http: //support.google.com/mail/bin/answer.py?answer=6596 do3si8775385pbc.262 - gsmtp \",\r\n        \"purchase\": \"PO1452297845\",\r\n        \"event\": \"bounce\",\r\n        \"email\": \"asdfasdflksjfe@sendgrid.com\",\r\n        \"timestamp\": 1386637483,\r\n        \"smtp-id\": \"<142da08cd6e.5e4a.310b89@localhost.localdomain>\",\r\n        \"type\": \"bounce\",\r\n        \"category\": [\r\n            \"category1\",\r\n            \"category2\",\r\n            \"category3\"\r\n        ],\r\n        \"id\": \"001\"\r\n    },\r\n    {\r\n        \"email\": \"john.doe@gmail.com\",\r\n        \"timestamp\": 1386638248,\r\n        \"uid\": \"123456\",\r\n        \"purchase\": \"PO1452297845\",\r\n        \"id\": \"001\",\r\n        \"category\": [\r\n            \"category1\",\r\n            \"category2\",\r\n            \"category3\"\r\n        ],\r\n        \"event\": \"unsubscribe\"\r\n    }\r\n]";

		SendgridWebHookResource.parseJson(json);

	}

}