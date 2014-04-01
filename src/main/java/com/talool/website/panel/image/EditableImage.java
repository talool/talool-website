package com.talool.website.panel.image;

import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
import com.talool.website.component.StaticImage;
import com.talool.website.component.UploadIFrame;
import com.talool.website.util.CssClassToggle;

abstract public class EditableImage extends Panel {

	private static final long serialVersionUID = -8867055443252583175L;
	private static final Logger LOG = LoggerFactory.getLogger(EditableImage.class);
	
	private static final String showUploader = "showUploader";
	private static final String hideUploader = "hideUploader";
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	private UploadIFrame iframe;
	private UUID merchantId;
	private MediaType mediaType;
	private int iframeHeight = 100;
	private int iframeWidth = 320;
	private PageParameters params = new PageParameters();

	public EditableImage(String id, IModel<String> model, UUID merchantId, MediaType mediaType) {
		super(id, model);
		
		this.merchantId = merchantId;
		this.mediaType = mediaType;
		params.add("id", merchantId);
		params.add("type", mediaType);
		
		setOutputMarkupId(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		IModel<String> model = (Model<String>)getDefaultModel();
		boolean missingImage = (model.getObject()==null);
		if (missingImage) model.setObject("/img/000.png");
		add(new StaticImage("image",true,model).setOutputMarkupId(true));
		

		iframe = new UploadIFrame("upload", params)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onUploadComplete(AjaxRequestTarget target, String url)
			{
				final MerchantMedia merchantMedia = domainFactory.newMedia(merchantId, url, mediaType);
				saveMedia(target, merchantMedia);
				onMediaUploadComplete(target, merchantMedia);
			}

		};
		
		iframe.add(new AttributeAppender("height", new Model<Integer>(iframeHeight)));
		iframe.add(new AttributeAppender("width", new Model<Integer>(iframeWidth)));
		
		add(iframe.setOutputMarkupId(true));
		
		add(new AjaxLink<String>("toggle"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				target.add(toggle(true));
			}
			
		});
		
		toggle(missingImage);
	}
	
	private Panel toggle(boolean show)
	{
		if (show)
		{
			add(new CssClassToggle(hideUploader,showUploader));
		}
		else
		{
			add(new CssClassToggle(showUploader,hideUploader));
		}
		return this;
	}
	
	private void saveMedia(AjaxRequestTarget target, MerchantMedia media)
	{
		try
		{
			taloolService.saveMerchantMedia(media);
			setDefaultModelObject(media.getMediaUrl());
			target.add(toggle(false));
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
