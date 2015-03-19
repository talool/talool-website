package com.talool.website.util;

import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.ServiceFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class PublisherCobrand implements Serializable {
	
	private static final long serialVersionUID = -8512739345275628862L;

	private static final Logger LOG = Logger.getLogger(PublisherCobrand.class);

	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public static final String COBRAND_PROPERTY_KEY = "cobrand";
	
	private String cobrandName;
	private Merchant publisher;
	private boolean hasCobrand = false;
	
	public PublisherCobrand(String cobrandName) {
		super();


		// search for merchants by the cobrand property and return the first we find
		try {
			List<Merchant> merchants = (List<Merchant>) taloolService
					.getEntityByProperty(Merchant.class, COBRAND_PROPERTY_KEY, cobrandName);
			if (!merchants.isEmpty())
			{
				publisher = merchants.get(0);
				this.cobrandName = cobrandName;
				hasCobrand = true;
			}
		}
		catch (ServiceException e)
		{
			LOG.debug("Failed to find a cobrand for "+cobrandName, e);
		}
	}

	public PublisherCobrand(UUID id) {
		super();

		try {
			publisher = ServiceFactory.get().getTaloolService().getMerchantById(id);
			cobrandName = publisher.getProperties().getAsString(COBRAND_PROPERTY_KEY);
			hasCobrand = StringUtils.isNotEmpty(cobrandName);
		}
		catch (ServiceException e)
		{
			LOG.debug("Failed to find a cobrand for UUID: "+id, e);
		}
	}

	public PublisherCobrand(Merchant publisher) {
		super();
		this.publisher = publisher;
		cobrandName = publisher.getProperties().getAsString(COBRAND_PROPERTY_KEY);
		hasCobrand = StringUtils.isNotEmpty(cobrandName);
	}

	public String getCobrandName() {
		if (hasCobrand)
			return cobrandName;
		else
			return "sales";
	}

	public Merchant getPublisher() {
		return publisher;
	}

	public boolean hasCobrand() {
		return hasCobrand;
	}
}
