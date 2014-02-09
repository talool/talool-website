package com.talool.website.panel.image.selection;

import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.component.UploadIFrame;

abstract public class MediaUploadTab extends Panel
{

	private static final long serialVersionUID = -4183138880833396304L;
	private static final Logger LOG = LoggerFactory.getLogger(MediaUploadTab.class);
	private PageParameters params = new PageParameters();
	private UUID merchantId;
	private MediaType mediaType;

	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public MediaUploadTab(String id, UUID merchantId, MediaType mediaType)
	{
		super(id);
		this.merchantId = merchantId;
		this.mediaType = mediaType;
		params.add("id", merchantId);
		params.add("type", mediaType);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new UploadIFrame("uploaderIFrame", params)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onUploadComplete(AjaxRequestTarget target, String url)
			{
				final MerchantMedia merchantMedia = domainFactory.newMedia(merchantId, url, mediaType);
				saveMedia(merchantMedia);
				onMediaUploadComplete(target, merchantMedia);
			}

		});
		
		
	}


	private void saveMedia(MerchantMedia media)
	{
		try
		{
			taloolService.saveMerchantMedia(media);
		}
		catch (ServiceException se)
		{
			LOG.error("failed to save media:", se);
		}
		catch (DataIntegrityViolationException dve)
		{
			// ERROR: duplicate key value violates unique constraint
			// "merchant_media_merchant_id_media_url_key"
			LOG.info("merchant tried to upload the same image twice");
		}
		catch (Exception e)
		{
			LOG.error("random-ass-exception saving new merchant media:", e);
		}
	}

	abstract public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media);

}
