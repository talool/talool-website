package com.talool.website.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantMediaListModel extends LoadableDetachableModel<List<MerchantMedia>>
{

	private static final long serialVersionUID = -1537678799039334997L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantMediaListModel.class);
	private UUID _merchantId = null;
	private MediaType _mediaType = null;

	@Override
	protected List<MerchantMedia> load()
	{
		List<MerchantMedia> media = null;

		try
		{
			if (_merchantId != null)
			{
				SearchOptions searchOptions = new SearchOptions.Builder().maxResults(100).page(0).sortProperty("merchantMedia.mediaUrl")
						.ascending(true).build();

				media = ServiceFactory.get().getTaloolService().getMerchantMedias(_merchantId, searchOptions);
			}
			else
			{
				media = new ArrayList<MerchantMedia>();
				
				// TODO do we want to have a default media?
				media.add(getDefaultMedia());
			}
		}
		catch (ServiceException e)
		{
			LOG.error("problem loading merchants", e);
		}

		return media;
	}

	public void setMerchantId(final UUID id)
	{
		_merchantId = id;
	}
	
	public void setMediaType(final MediaType mt)
	{
		_mediaType = mt;
	}
	
	private MerchantMedia getDefaultMedia()
	{
		DomainFactory domainFactory = FactoryManager.get().getDomainFactory();
		MediaType mt = (_mediaType==null)?MediaType.DEAL_IMAGE:_mediaType;
		MerchantMedia media = domainFactory.newMedia(null, "?filename=youshoulduploadsomething.png", mt);
		return media;
	}

}
