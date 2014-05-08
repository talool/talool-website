package com.talool.website.marketing.pages;

import java.io.Serializable;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.marketing.panel.landing.MerchantOutreachLocal;
import com.talool.website.marketing.panel.landing.MerchantOutreachNational;
import com.talool.website.marketing.panel.landing.PTOOutreachBooks;
import com.talool.website.marketing.panel.landing.PTOOutreachBooksCrowdSource;

public class LandingPagePanelPicker implements Serializable {

	private static final long serialVersionUID = 8390824646913457971L;
	private static final Logger LOG = LoggerFactory.getLogger(LandingPagePanelPicker.class);
	private enum landingPageType {MERCHANT_OUTREACH, PTO_OUTREACH};
	private String lp;
	
	public landingPageType landingPage;
	public int variation;
	public int recipient;
	
	public LandingPagePanelPicker(PageParameters parameters)
	{
		try
		{
			lp = parameters.get(0).toString();
			variation = parameters.get(1).toInt();
			recipient = parameters.get(2).toInt();
			
			if (lp.equalsIgnoreCase("m") && variation >= 0 && variation < 4)
			{
				landingPage = landingPageType.MERCHANT_OUTREACH;
			}
			else if (lp.equalsIgnoreCase("p") && variation >= 0 && variation < 4)
			{
				landingPage = landingPageType.PTO_OUTREACH;
			}
		}
		catch (Exception e)
		{
			LOG.error("failed to parse landing page parameters",e);
		}
	}
	
	public Panel getPanel(String contentId) throws Exception
	{
		Panel p;
		if (landingPage == landingPageType.MERCHANT_OUTREACH)
		{
			switch (variation)
			{
				case 1: 
					p = new MerchantOutreachLocal(contentId, 90);
					break;
				case 2:
					p = new MerchantOutreachNational(contentId, 80);
					break;
				case 3:
					p = new MerchantOutreachNational(contentId, 90);
					break;
				default:
					p = new MerchantOutreachLocal(contentId, 80);
			}
			
		}
		else if (landingPage == landingPageType.PTO_OUTREACH)
		{
			switch (variation)
			{
				case 1: 
					p = new PTOOutreachBooks(contentId, 80);
					break;	
				case 2: 
					p = new PTOOutreachBooksCrowdSource(contentId, 80);
					break;
				case 3:
					p = new PTOOutreachBooksCrowdSource(contentId, 90);
					break;
				default:
					p = new PTOOutreachBooks(contentId, 90);
			}
		}
		else
		{
			throw new Exception("invalid parameters");
		}
		return p;
	}
}
