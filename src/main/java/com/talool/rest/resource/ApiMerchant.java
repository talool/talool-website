package com.talool.rest.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.wicketstuff.rest.annotations.MethodMapping;
import org.wicketstuff.rest.annotations.ResourcePath;

import com.talool.core.Merchant;
import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.domain.PropertyCriteria;
import com.talool.domain.PropertyCriteria.Filter;
import com.talool.rest.domain.MerchantPojo;
import com.talool.stats.MerchantSummary;
import com.talool.stats.PaginatedResult;
import com.talool.utils.KeyValue;

@ResourcePath("/api/merchants")
public class ApiMerchant extends ApiTaloolResource 
{
	private static final Logger LOG = Logger.getLogger(ApiMerchant.class);
	private static final long serialVersionUID = 1L;
	
	@MethodMapping("/book/{bookId}")
	public List<MerchantPojo> getMerchantsInBook(String bookId)
	{
		List<Merchant> merchants = new ArrayList<Merchant>();
		List<MerchantPojo> pojos = new ArrayList<MerchantPojo>();
		try 
		{
			UUID dealOfferId = UUID.fromString(bookId);
			SearchOptions searchOptions = new SearchOptions.Builder().maxResults(500).page(0).sortProperty("merchant.name").build();
			merchants = taloolService.getMerchantsByDealOfferId(dealOfferId, searchOptions);
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get merchants", se);
		}
		
		for (Merchant m : merchants)
		{
			pojos.add(new MerchantPojo(m));
		}
		return pojos;
	}
	
	@MethodMapping("/publisher/{publisherId}/fundraisers")
	public List<MerchantPojo> getFundraisersByPublisher(String publisherId)
	{
		PaginatedResult<MerchantSummary> merchants = null;
		List<MerchantPojo> pojos = new ArrayList<MerchantPojo>();
		try 
		{
			UUID id = UUID.fromString(publisherId);
			PropertyCriteria criteria = new PropertyCriteria();
			criteria.setFilters(Filter.equal(KeyValue.fundraiser, true));
			SearchOptions searchOptions = new SearchOptions.Builder().maxResults(500).page(0).sortProperty("name").build();
			merchants = taloolService.getPublisherMerchantSummary(id, searchOptions, criteria, true);
			if (merchants != null)
			{
				List<MerchantSummary> results = merchants.getResults();
				for (MerchantSummary m : results)
				{
					pojos.add(new MerchantPojo(m));
				}
			}
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get merchants", se);
		}
		
		
		
		return pojos;
	}

}
