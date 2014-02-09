package com.talool.website.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantMedia;
import com.talool.core.SearchOptions;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
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
	private static final String talool = "Talool";
	private UUID _merchantId = null;
	private UUID _taloolMerchantId = null;
	private MediaType _mediaType = null;
	
	private static final transient TaloolService taloolService = ServiceFactory.get().getTaloolService();

	
	public MerchantMediaListModel() {
		super();
		
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

		try
		{
			SearchOptions searchOptions = new SearchOptions.Builder().maxResults(100).page(0).sortProperty("merchantMedia.mediaUrl")
					.ascending(true).build();
			
			MediaType[] mediaTypes;
			if (_mediaType == null)
			{
				mediaTypes = MediaType.values();
			}
			else
			{
				if (_mediaType.equals(MediaType.DEAL_IMAGE) || _mediaType.equals(MediaType.MERCHANT_IMAGE))
				{
					mediaTypes = new MediaType[]{MediaType.DEAL_IMAGE,MediaType.MERCHANT_IMAGE};
				}
				else if (_mediaType.equals(MediaType.DEAL_OFFER_LOGO) || _mediaType.equals(MediaType.MERCHANT_LOGO))
				{
					mediaTypes = new MediaType[]{MediaType.DEAL_OFFER_LOGO,MediaType.MERCHANT_LOGO};
				}
				else if (_mediaType.equals(MediaType.DEAL_OFFER_BACKGROUND_IMAGE))
				{
					mediaTypes = new MediaType[]{MediaType.DEAL_OFFER_BACKGROUND_IMAGE};
				}
				else if (_mediaType.equals(MediaType.DEAL_OFFER_MERCHANT_LOGO))
				{
					mediaTypes = new MediaType[]{MediaType.DEAL_OFFER_MERCHANT_LOGO};
				}
				else
				{
					mediaTypes = MediaType.values();
				}
			}
			
			media = new ArrayList<MerchantMedia>();
			
			if (_mediaType.equals(MediaType.DEAL_OFFER_BACKGROUND_IMAGE) || _mediaType.equals(MediaType.DEAL_OFFER_MERCHANT_LOGO))
			{
				media.addAll(taloolService.getMerchantMedias(_taloolMerchantId, mediaTypes, searchOptions));
				media.addAll(taloolService.getMerchantMedias(_merchantId, mediaTypes, searchOptions));
			}
			else if (_merchantId == null)
			{
				// add the stock media
				media.addAll(taloolService.getMerchantMedias(_taloolMerchantId, mediaTypes, searchOptions));
			}
			else
			{
				media.addAll(taloolService.getMerchantMedias(_merchantId, mediaTypes, searchOptions));
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

}
