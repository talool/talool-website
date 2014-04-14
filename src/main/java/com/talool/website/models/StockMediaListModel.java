package com.talool.website.models;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantMedia;
import com.talool.core.SearchOptions;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class StockMediaListModel extends LoadableDetachableModel<List<MerchantMedia>>
{

	private static final long serialVersionUID = -1537678799039334997L;
	private static final Logger LOG = LoggerFactory.getLogger(StockMediaListModel.class);
	private static final String talool = "Talool";
	private UUID _taloolMerchantId = null;
	private Collection<Tag> _tags;
	
	private static final transient TaloolService taloolService = ServiceFactory.get().getTaloolService();

	
	public StockMediaListModel(Collection<Tag> tags) {
		super();
		
		_tags = tags;
		
		try 
		{
			List<Merchant> merchants = taloolService.getMerchantByName(talool);
			_taloolMerchantId = merchants.get(0).getId();
		} 
		catch (ServiceException se)
		{
			LOG.error("problem fetcing the talool merchant id", se);
		}
	}

	@Override
	protected List<MerchantMedia> load()
	{
		List<MerchantMedia> media = null;
		
		SearchOptions searchOptions = new SearchOptions.Builder().maxResults(500).page(0).sortProperty("merchantMedia.mediaUrl")
				.ascending(true).build();

		try
		{
			media = taloolService.getStockMedias(_taloolMerchantId, (Set<Tag>)_tags, searchOptions);
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading stock media", e);
		}

		return media;
	}
	
}
