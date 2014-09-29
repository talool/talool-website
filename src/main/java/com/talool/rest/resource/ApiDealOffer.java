package com.talool.rest.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.wicketstuff.rest.annotations.MethodMapping;
import org.wicketstuff.rest.annotations.ResourcePath;

import com.talool.core.DealOffer;
import com.talool.core.service.ServiceException;
import com.talool.rest.domain.DealOfferPojo;

@ResourcePath("/api/books")
public class ApiDealOffer extends ApiTaloolResource 
{
	private static final Logger LOG = Logger.getLogger(ApiDealOffer.class);
	private static final long serialVersionUID = 1L;
	
	@MethodMapping("")
	public List<DealOfferPojo> getAllBooks()
	{
		List<DealOffer> books = new ArrayList<DealOffer>();
		List<DealOfferPojo> pojos = new ArrayList<DealOfferPojo>();
		try 
		{
			books = taloolService.getDealOffers();
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get books", se);
		}
		
		for (DealOffer book : books)
		{
			pojos.add(new DealOfferPojo(book));
		}
		return pojos;
	}
	
	@MethodMapping("/{id}")
	public DealOfferPojo getBook(String id)
	{
		DealOffer book = null;
		DealOfferPojo pojo = null;
		try 
		{
			UUID dealOfferId = UUID.fromString(id);
			book = taloolService.getDealOffer(dealOfferId);
		}
		catch (ServiceException se)
		{
			LOG.error("Failed to get book", se);
		}
		
		if (book != null)
		{
			pojo = new DealOfferPojo(book);
		}
		return pojo;
	}

}
